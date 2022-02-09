package fr.versiontracker2.api.resource;

import lombok.Data;

@Data
public class TrackedDependencyInfo {

    private String dependency;
    private String version;

}
