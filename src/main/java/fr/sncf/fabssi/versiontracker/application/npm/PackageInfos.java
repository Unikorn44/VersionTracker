package fr.sncf.fabssi.versiontracker.application.npm;


import java.util.Map;


public class PackageInfos {

    private String name;
    private String version;
    private Map<String, String> dependencies;


    public String getName() {
        return name;
    }

    public String getVersion(){
        return version;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }
}

