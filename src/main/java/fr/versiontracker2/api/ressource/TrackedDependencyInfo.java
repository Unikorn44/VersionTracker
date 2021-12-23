package fr.versiontracker2.api.ressource;

import lombok.Data;

@Data
public class TrackedDependencyInfo {

    private String dependency;
    private String version;

}
