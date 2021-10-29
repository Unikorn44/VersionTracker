package fr.versiontracker.api.ressource;

import lombok.Data;

import java.util.List;

@Data
public class Projet {

    private String name;
    private String fileUrl;
    private String packageManager;
    public List<TrackedDependencyInfo> trackedDependencyInfos;

}
