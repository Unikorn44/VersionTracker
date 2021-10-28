package fr.sncf.fabssi.versiontracker.application;

import fr.sncf.fabssi.versiontracker.configuration.TrackedDependencyInfo;

import java.util.List;

public class Projet {

    private String name;
    private String fileUrl;
    private String packageManager;
    public List<TrackedDependencyInfo> trackedDependencyInfos;

    @Override
    public String toString() {
        return "Project {" +
                "name='" + name + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", packageManager='" + packageManager + '\'' +
                ", liste dependencies et version'" + trackedDependencyInfos +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getPackageManager() {
        return packageManager;
    }

    public void setPackageManager(String packageManager) {
        this.packageManager = packageManager;
    }

    public List<TrackedDependencyInfo> getDependencyInfos() {
        return trackedDependencyInfos;
    }

    public void setDependencyInfos(List<TrackedDependencyInfo> trackedDependencyInfos) {
        this.trackedDependencyInfos = trackedDependencyInfos;
    }
}
