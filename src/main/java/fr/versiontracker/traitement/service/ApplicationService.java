package fr.versiontracker.traitement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.versiontracker.traitement.modele.ApplicationConfiguration;
import fr.versiontracker.traitement.modele.Dependency;
import fr.versiontracker.traitement.modele.MavenDependency;
import fr.versiontracker.traitement.modele.ProjectConfiguration;
import fr.versiontracker.traitement.service.maven.MavenExtractionService;
import fr.versiontracker.traitement.service.npm.NPMExtractionService;
import fr.versiontracker.transverse.exception.NonReadableApplicationConfigurationException;
import fr.versiontracker.transverse.exception.NonReadableDependencyFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApplicationService {

    @Autowired
    MavenExtractionService mavenExtractionService;

    @Autowired
    NPMExtractionService npmExtractionService;

    public List<ApplicationConfiguration> getInfoApplication() throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        List<URL> applicationListURLs = new ArrayList<>();
        try {
            applicationListURLs.add(new URL("file:///C:/CODE/Java/XMLRush.json"));
            applicationListURLs.add(new URL("file:///C:/CODE/Java/XMLRush1.json"));
        } catch (MalformedURLException e) {
            log.error("Erreur lors de la prise en compte d'une URL de fichier Rush", e);
        }

        List<ApplicationConfiguration> listApplicationConfigurations = new ArrayList<>();

        for (URL applicationListURL : applicationListURLs) {
            String fileApplicationName = new File(applicationListURL.toString()).getName();
            ApplicationConfiguration applicationConfiguration = this.getFileProjectInfo(applicationListURL);
            applicationConfiguration.setFileApplicationName(fileApplicationName);
            listApplicationConfigurations.add(applicationConfiguration);
        }
        return listApplicationConfigurations;
    }

    private ApplicationConfiguration getFileProjectInfo(URL fileURL) throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        ApplicationConfiguration applicationConfiguration = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            applicationConfiguration = objectMapper.readValue(fileURL, ApplicationConfiguration.class);
        } catch (IOException e) {
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
