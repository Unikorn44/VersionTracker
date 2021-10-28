package fr.sncf.fabssi.versiontracker.configuration;

import java.io.Serializable;
import java.util.List;

public class ProjectConfiguration implements Serializable {

    private String name;
    private String fileUrl;
    private String packageManager;
    private List<String> trackedDependencies;

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", packageManager='" + packageManager + '\'' +
                ", trackedDependencies=" + trackedDependencies +
                '}';
    }


    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getFileUrl(){
        return this.fileUrl;
    }

    public void setFileUrl(String fileURL){
        this.fileUrl = fileURL;
    }

    public String getPackageManager(){
        return this.packageManager;
    }

    public void setPackageManager(String packageManager){
        this.packageManager = packageManager;
    }

    public List<String> getTrackedDependencies(){
        return this.trackedDependencies;
    }

    public void setTrackedDependencies(List<String> trackedDependencies) {
        this.trackedDependencies = trackedDependencies;
    }


}
