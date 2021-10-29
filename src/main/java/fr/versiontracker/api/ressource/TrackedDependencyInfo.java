package fr.versiontracker.api.ressource;

import lombok.Data;

@Data
public class TrackedDependencyInfo {

    //TODO : rename to name
    private String dependency;
    private String version;

}
