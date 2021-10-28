package fr.sncf.fabssi.versiontracker.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sncf.fabssi.versiontracker.configuration.ProjectConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetFileInfo {

    public static List<Projet> getFileProjectInfo(URL fileURL) throws Exception {

        String fileName = new File(fileURL.toString()).getName();

        InfoRetriever fileInfo = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            fileInfo = objectMapper.readValue(fileURL, InfoRetriever.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ProjectConfiguration[] projectConfigurations = fileInfo.getProjects();

        List<Projet> listProjets = new ArrayList();

        for (ProjectConfiguration projectConfiguration : projectConfigurations){

            Projet treatedProjet = new Projet();

            treatedProjet.setName(projectConfiguration.getName());
            treatedProjet.setFileUrl(projectConfiguration.getFileUrl());
            System.out.println("Da URL :" + projectConfiguration.getFileUrl());
            treatedProjet.setPackageManager(projectConfiguration.getPackageManager());

            DependenciesProcessing.extractTrackedDependencies(treatedProjet, projectConfiguration);

            listProjets.add(treatedProjet);
        }

        return listProjets;
    }
}
