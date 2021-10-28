package fr.sncf.fabssi.versiontracker.application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetProjectsForApplication {


    public static List getInfoApplication() throws Exception {

        List<URL> applicationListURLs = new ArrayList<URL>();
            applicationListURLs.add(new URL("file:///C:/Users/7309087K/Desktop/AppliJava/XMLRush.json"));
            applicationListURLs.add(new URL("file:///C:/Users/7309087K/Desktop/AppliJava/XMLRush1.json"));
            //applicationListURLs.add(new URL("file:///C:/Users/7309087K/Desktop/TRANFERT_Java/AppliJava/XMLRush2.json"));

        List<FileApplication> listFileApplications = new ArrayList();

        for (URL applicationListURL : applicationListURLs) {
            FileApplication createdFileApplication = new FileApplication();

            String fileApplicationName = new File(applicationListURL.toString()).getName();
            createdFileApplication.setFileApplicationName(fileApplicationName);

            List<Projet> listProjets = new ArrayList();
            listProjets = GetFileInfo.getFileProjectInfo(applicationListURL);
            System.out.println("Da projects list :" + listProjets);
            createdFileApplication.setListProjets(listProjets);

            listFileApplications.add(createdFileApplication);
        }
        return listFileApplications;
    }

}
