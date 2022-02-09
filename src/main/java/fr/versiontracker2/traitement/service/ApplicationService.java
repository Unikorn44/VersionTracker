package fr.versiontracker2.traitement.service;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import fr.versiontracker2.traitement.modele.ApplicationConfiguration;
import fr.versiontracker2.traitement.modele.Dependency;
import fr.versiontracker2.traitement.modele.MavenDependency;
import fr.versiontracker2.traitement.modele.ProjectConfiguration;
import fr.versiontracker2.traitement.modele.StringList;
import fr.versiontracker2.traitement.service.maven.MavenExtractionService;
import fr.versiontracker2.traitement.service.npm.NPMExtractionService;
import fr.versiontracker2.transverse.exception.NonReadableApplicationConfigurationException;
import fr.versiontracker2.transverse.exception.NonReadableDependencyFileException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApplicationService {

    public static final String CONTENT = "/content";
    @Autowired
    MavenExtractionService mavenExtractionService;

    @Autowired
    NPMExtractionService npmExtractionService;

    @Autowired
    private WebClient webClient;

    /**
     * Reçoit la liste des applications puis les informations sur celles-ci
     * @return Liste des configurations d'applications
     * @throws NonReadableApplicationConfigurationException si une configuration n'a pas pas pu être exploitée
     * @throws NonReadableDependencyFileException si une dépendance n'a pas pu être exploitée
     */
    public List<ApplicationConfiguration> getInfoApplication(OAuth2AuthorizedClient authorizedClient) 
    		throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        String rushsResourceUrl
                = "http://localhost:8090/rushs/";

        List<String> applicationFilenameList = this.webClient
                .get()
                .uri("http://127.0.0.1:8090/rushs")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(StringList.class)
                .block();

        List<ApplicationConfiguration> listApplicationConfigurations = new ArrayList<>();
        if (applicationFilenameList != null) {
            for (String applicationFilename : applicationFilenameList) {
                ApplicationConfiguration applicationConfiguration = 
                		this.getFileProjectInfo(applicationFilename, rushsResourceUrl, authorizedClient);
                Optional.ofNullable(applicationConfiguration).ifPresent(a -> a.setFileApplicationName(applicationFilename));
                listApplicationConfigurations.add(applicationConfiguration);
            }
        }
        return listApplicationConfigurations;
    }

    /**
     * Reçoit le nom du fichier à analyser, puis la configuration de l'application
     * @param fileName nom du fichier
     * @param rushsResourceUrl
     * @return Liste des dépendances
     * @throws NonReadableApplicationConfigurationException
     * @throws NonReadableDependencyFileException
     */
    private ApplicationConfiguration getFileProjectInfo(String fileName, String rushsResourceUrl, OAuth2AuthorizedClient authorizedClient)
    		throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        ApplicationConfiguration applicationConfiguration = null;

        try {
            applicationConfiguration = this.webClient
                    .get()
                    .uri(rushsResourceUrl + fileName + CONTENT)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(ApplicationConfiguration.class)
                    .block();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Impossible de lire la configuration d'application", e);
            throw new NonReadableApplicationConfigurationException("Impossible de lire la configuration d'application", e);
        }

        if (applicationConfiguration != null) {
	        List<ProjectConfiguration> projectConfigurations = applicationConfiguration.getProjectConfigurations();
	
	        for (ProjectConfiguration projectConfiguration : projectConfigurations) {
	            projectConfiguration.setDependencies(this.extractTrackedDependencies(projectConfiguration, authorizedClient));
	        }
        }
        return applicationConfiguration;
    }

    /**
     * Reçoit la liste des dépendances recherchées
     * @param projectConfiguration
     * @param authorizedClient
     * @return la liste des dépendances
     * @throws NonReadableDependencyFileException
     */
    private List<Dependency> extractTrackedDependencies(ProjectConfiguration projectConfiguration, OAuth2AuthorizedClient authorizedClient) 
    		throws NonReadableDependencyFileException {

        List<String> trackedDependencies = projectConfiguration.getTrackedDependencies();
        List<Dependency> listDependencies = new ArrayList<>();

        for (String trackedDependency : trackedDependencies) {
            if (projectConfiguration.getPackageManager().equals("MAVEN")) {
                MavenDependency mavenDependency = mavenExtractionService.extractMavenDependencyFrom(trackedDependency);
                Dependency dependency = new Dependency();
                dependency.setName(trackedDependency);
                dependency.setVersion(mavenExtractionService.getVersionWithPP3(projectConfiguration.getFileUrl(), mavenDependency, authorizedClient));
                listDependencies.add(dependency);
            } else if (projectConfiguration.getPackageManager().equals("NPM")) {
                Dependency dependency = npmExtractionService.getVersionWithJackson(projectConfiguration.getFileUrl(), trackedDependency, authorizedClient);
                listDependencies.add(dependency);
            } else log.error("unknown Package Manager : " + trackedDependency);
        }
        return listDependencies;
    }

}
