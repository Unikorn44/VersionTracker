package fr.versiontracker.api.mapper;

import fr.versiontracker.api.ressource.FileApplication;
import fr.versiontracker.api.ressource.Projet;
import fr.versiontracker.api.ressource.TrackedDependencyInfo;
import fr.versiontracker.traitement.modele.ApplicationConfiguration;
import fr.versiontracker.traitement.modele.Dependency;
import fr.versiontracker.traitement.modele.ProjectConfiguration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {

    public List<FileApplication> transformeModeleEnRessource(List<ApplicationConfiguration> modele) {
        List<FileApplication> fileApplications = new ArrayList<>();
        modele.forEach(a -> {
            FileApplication fileApplication = new FileApplication();
            fileApplication.setFileApplicationName(a.getFileApplicationName());
            fileApplication.setListProjets(this.transformeProjectConfigurationEnProjet(a.getProjectConfigurations()));
            fileApplications.add(fileApplication);
        });
        return fileApplications;
    }

    private List<Projet> transformeProjectConfigurationEnProjet(List<ProjectConfiguration> modele) {
        List<Projet> projets = new ArrayList<>();
        modele.forEach(p -> {
            Projet projet = new Projet();
            projet.setName(p.getName());
            projet.setFileUrl(p.getFileUrl());
            projet.setPackageManager(p.getPackageManager());
            projet.setTrackedDependencyInfos(this.transformeDependencyEnTrackedDependencyInfo(p.getDependencies()));
            projets.add(projet);
        });
        return projets;
    }

    private List<TrackedDependencyInfo> transformeDependencyEnTrackedDependencyInfo(List<Dependency> modele) {
        List<TrackedDependencyInfo> trackedDependencyInfos = new ArrayList<>();
        modele.forEach(d -> {
            TrackedDependencyInfo trackedDependencyInfo = new TrackedDependencyInfo();
            trackedDependencyInfo.setDependency(d.getName());
            trackedDependencyInfo.setVersion(d.getVersion());
            trackedDependencyInfos.add(trackedDependencyInfo);
        });
        return trackedDependencyInfos;
    }

}
