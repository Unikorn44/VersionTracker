package fr.versiontracker.traitement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.versiontracker.api.ressource.FileApplication;
import fr.versiontracker.api.ressource.Projet;
import fr.versiontracker.traitement.modele.ApplicationConfiguration;
import fr.versiontracker.traitement.modele.Dependency;
import fr.versiontracker.traitement.modele.MavenDependency;
import fr.versiontracker.traitement.modele.ProjectConfiguration;
import fr.versiontracker.traitement.service.maven.MavenExtractionService;
import fr.versiontracker.traitement.service.npm.NPMExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    MavenExtractionService mavenExtractionService;

    @Autowired
    NPMExtractionService npmExtractionService;

    public List<ApplicationConfiguration> getInfoApplication() throws Exception {

        List<URL> applicationListURLs = new ArrayList<URL>();
            applicationListURLs.add(new URL("file:///C:/CODE/Java/XMLRush.json"));
            applicationListURLs.add(new URL("file:///C:/CODE/Java/XMLRush1.json"));

        //List<FileApplication> listFileApplications = new ArrayList();
        List<ApplicationConfiguration> listApplicationConfigurations = new ArrayList<>();

        for (URL applicationListURL : applicationListURLs) {
            //FileApplication createdFileApplication = new FileApplication();

            String fileApplicationName = new File(applicationListURL.toString()).getName();
            //createdFileApplication.setFileApplicationName(fileApplicationName);

            //List<Projet> listProjets = new ArrayList();
            ApplicationConfiguration applicationConfiguration = this.getFileProjectInfo(applicationListURL);
            applicationConfiguration.setFileApplicationName(fileApplicationName);
            listApplicationConfigurations.add(applicationConfiguration);

//            createdFileApplication.setListProjets(listProjets);

 //           listFileApplications.add(createdFileApplication);
        }
        return listApplicationConfigurations;
    }

    private ApplicationConfiguration getFileProjectInfo(URL fileURL) throws Exception {

        String fileName = new File(fileURL.toString()).getName();

        ApplicationConfiguration applicationConfiguration = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            applicationConfiguration = objectMapper.readValue(fileURL, ApplicationConfiguration.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<ProjectConfiguration> projectConfigurations = applicationConfiguration.getProjectConfigurations();
        //List<Projet> listProjets = new ArrayList();

        for (ProjectConfiguration projectConfiguration : projectConfigurations){

//            Projet treatedProjet = new Projet();
//
//            treatedProjet.setName(projectConfiguration.getName());
//            treatedProjet.setFileUrl(projectConfiguration.getFileUrl());
//            treatedProjet.setPackageManager(projectConfiguration.getPackageManager());

            projectConfiguration.setDependencies(this.extractTrackedDependencies(projectConfiguration));

            //listProjets.add(treatedProjet);
        }

        return applicationConfiguration;
    }

    private List<Dependency> extractTrackedDependencies(ProjectConfiguration projectConfiguration) throws Exception {

        List<String> trackedDependencies = projectConfiguration.getTrackedDependencies();
        //List<TrackedDependencyInfo> listTrackedDependencyInfos = new ArrayList<TrackedDependencyInfo>();
        List<Dependency> listDependencies = new ArrayList<>();

        for (String trackedDependency : trackedDependencies){
            //TrackedDependencyInfo newTrackedDependencyinfo = new TrackedDependencyInfo();

            if (projectConfiguration.getPackageManager().equals("MAVEN")){
                //newTrackedDependencyinfo.setDependency(trackedDependency);
                MavenDependency mavenDependency = mavenExtractionService.extractMavenDependencyFrom(trackedDependency);
                Dependency dependency = new Dependency();
                dependency.setName(trackedDependency);
                dependency.setVersion(mavenExtractionService.getVersionWithPP3(projectConfiguration.getFileUrl(), mavenDependency));
                //listTrackedDependencyInfos.add(newTrackedDependencyinfo);
                listDependencies.add(dependency);
            }
            else if (projectConfiguration.getPackageManager().equals("NPM")){
                //newTrackedDependencyinfo.setDependency(trackedDependency);
                Dependency dependency = npmExtractionService.getVersionWithJackson(projectConfiguration.getFileUrl(), trackedDependency);
                //listTrackedDependencyInfos.add(newTrackedDependencyinfo);
                listDependencies.add(dependency);
            }
            else System.out.println("unknown Package Manager : " + trackedDependency);
        }
        //createdProjet.setTrackedDependencyInfos(listTrackedDependencyInfos);
        return listDependencies;
    }


}
