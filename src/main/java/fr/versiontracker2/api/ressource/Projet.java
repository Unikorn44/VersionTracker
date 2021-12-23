package fr.versiontracker2.api.ressource;

import lombok.Data;

import java.util.List;

@Data
public class Projet {

    private String name;
    private String fileUrl;
    private String packageManager;
    private List<TrackedDependencyInfo> trackedDependencyInfos;

}
