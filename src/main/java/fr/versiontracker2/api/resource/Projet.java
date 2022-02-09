package fr.versiontracker2.api.resource;

import java.util.List;

import lombok.Data;

@Data
public class Projet {

    private String name;
    private String fileUrl;
    private String packageManager;
    private List<TrackedDependencyInfo> trackedDependencyInfos;

}
