package fr.versiontracker.traitement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.versiontracker.traitement.modele.*;
import fr.versiontracker.traitement.service.maven.MavenExtractionService;
import fr.versiontracker.traitement.service.npm.NPMExtractionService;
import fr.versiontracker.transverse.exception.NonReadableApplicationConfigurationException;
import fr.versiontracker.transverse.exception.NonReadableDependencyFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplicationService {

    public static final String CONTENT = "/content";
    @Autowired
    MavenExtractionService mavenExtractionService;

    @Autowired
    NPMExtractionService npmExtractionService;

    @Autowired
    RestTemplate restTemplate;

    public List<ApplicationConfiguration> getInfoApplication() throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

//        try {
////            applicationListURLs.add(new URL("file:///C:/CODE/Java/XMLRush.json"));
////            applicationListURLs.add(new URL("file:///C:/CODE/Java/XMLRush1.json"));
//            applicationListURLs.add(new URL("http://localhost:8090/rushs/XMLRush.json/content"));
//            applicationListURLs.add(new URL("http://localhost:8090/rushs/XMLRush1.json/content"));
//        } catch (MalformedURLException e) {
//            log.error("Erreur lors de la prise en compte d'une URL de fichier Rush", e);
//        }

        String rushsResourceUrl
                = "http://localhost:8090/rushs/";
        ResponseEntity<StringList> response
                = restTemplate.getForEntity(rushsResourceUrl, StringList.class);
        List<String> applicationFilenameList = null;
        if ((response != null) && response.hasBody()) {
            applicationFilenameList = response.getBody();
        }

        List<ApplicationConfiguration> listApplicationConfigurations = new ArrayList<>();
        if (applicationFilenameList != null) {
            for (String applicationFilename : applicationFilenameList) {
                ApplicationConfiguration applicationConfiguration = this.getFileProjectInfo(applicationFilename, rushsResourceUrl);
                applicationConfiguration.setFileApplicationName(applicationFilename);
                listApplicationConfigurations.add(applicationConfiguration);
            }
        }
        return listApplicationConfigurations;
    }

    private ApplicationConfiguration getFileProjectInfo(String fileName, String rushsResourceUrl) throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        ApplicationConfiguration applicationConfiguration = null;

        try {
            ResponseEntity<ApplicationConfiguration> res = restTemplate.getForEntity(rushsResourceUrl + fileName + CONTENT, ApplicationConfiguration.class);
            if ((res != null) && HttpStatus.OK.equals(res.getStatusCode())) {
                applicationConfiguration = res.getBody();
            };
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Impossible de lire la configuration d'application", e);
            throw new NonReadableApplicationConfigurationException("Impossible de lire la configuration d'application", e);
        }

        List<ProjectConfiguration> projectConfigurations = applicationConfiguration.getProjectConfigurations();

        for (ProjectConfiguration projectConfiguration : projectConfigurations) {
            projectConfiguration.setDependencies(this.extractTrackedDependencies(projectConfiguration));
        }
        return applicationConfiguration;
    }

    private List<Dependency> extractTrackedDependencies(ProjectConfiguration projectConfiguration) throws NonReadableDependencyFileException {

        List<String> trackedDependencies = projectConfiguration.getTrackedDependencies();
        List<Dependency> listDependencies = new ArrayList<>();

        for (String trackedDependency : trackedDependencies) {
            if (projectConfiguration.getPackageManager().equals("MAVEN")) {
                MavenDependency mavenDependency = mavenExtractionService.extractMavenDependencyFrom(trackedDependency);
                Dependency dependency = new Dependency();
                dependency.setName(trackedDependency);
                dependency.setVersion(mavenExtractionService.getVersionWithPP3(projectConfiguration.getFileUrl(), mavenDependency));
                listDependencies.add(dependency);
            } else if (projectConfiguration.getPackageManager().equals("NPM")) {
                Dependency dependency = npmExtractionService.getVersionWithJackson(projectConfiguration.getFileUrl(), trackedDependency);
                listDependencies.add(dependency);
            } else log.error("unknown Package Manager : " + trackedDependency);
        }
        return listDependencies;
    }

}
