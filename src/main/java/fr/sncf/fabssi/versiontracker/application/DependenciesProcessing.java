package fr.sncf.fabssi.versiontracker.application;

import fr.sncf.fabssi.versiontracker.application.maven.MavenDependency;
import fr.sncf.fabssi.versiontracker.application.maven.MavenExtraction;
import fr.sncf.fabssi.versiontracker.application.npm.NPMExtraction;
import fr.sncf.fabssi.versiontracker.configuration.ProjectConfiguration;
import fr.sncf.fabssi.versiontracker.configuration.TrackedDependencyInfo;

import java.util.ArrayList;
import java.util.List;

public class DependenciesProcessing {

    public static void extractTrackedDependencies(Projet createdProjet, ProjectConfiguration projectConfiguration) throws Exception {

        List<String> valueTrackedDependencies = projectConfiguration.getTrackedDependencies();

        List<TrackedDependencyInfo> listTrackedDependencyInfos = new ArrayList<TrackedDependencyInfo>();

        for (String valueTrackedDependency : valueTrackedDependencies){
            TrackedDependencyInfo newTrackedDependencyinfo = new TrackedDependencyInfo();

            if (createdProjet.getPackageManager().equals("MAVEN")){
                newTrackedDependencyinfo.setDependency(valueTrackedDependency);
                MavenDependency mavenDependency = MavenExtraction.extractMavenDependencyFrom(valueTrackedDependency);

                MavenExtraction.GetVersionWithPP3(newTrackedDependencyinfo ,createdProjet.getFileUrl(), mavenDependency);
                listTrackedDependencyInfos.add(newTrackedDependencyinfo);
            }
            else if (createdProjet.getPackageManager().equals("NPM")){
                newTrackedDependencyinfo.setDependency(valueTrackedDependency);
                newTrackedDependencyinfo.setVersion(NPMExtraction.getVersionWithJackson(createdProjet.getFileUrl(), valueTrackedDependency));
                listTrackedDependencyInfos.add(newTrackedDependencyinfo);
            }
            else System.out.println("unknown Package Manager : " + valueTrackedDependency);
        }
        createdProjet.setDependencyInfos(listTrackedDependencyInfos);
    }
}

