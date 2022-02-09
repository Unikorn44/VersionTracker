package fr.versiontracker2.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.versiontracker2.api.resource.FileApplication;
import fr.versiontracker2.api.resource.Projet;
import fr.versiontracker2.api.resource.TrackedDependencyInfo;
import fr.versiontracker2.traitement.modele.ApplicationConfiguration;
import fr.versiontracker2.traitement.modele.Dependency;
import fr.versiontracker2.traitement.modele.ProjectConfiguration;

@Component
public class Mapper {

	/**
	 * Transforme une liste d'ApplicationConfiguration en une liste de FileApplication
	 * @param modele liste d'ApplicationConfiguration
	 * @return la liste des applications
	 */
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

    /**
     * Reçoit la liste des configuration de projets
     * @param modele
     * @return  la liste des projets
     */
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

    /**
     * Reçoit la liste des dépendances, 
     * @param modele
     * @return lal iste des dépendances et leur version
     */
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
