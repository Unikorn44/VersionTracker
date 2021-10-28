package fr.sncf.fabssi.versiontracker.application;

import fr.sncf.fabssi.versiontracker.configuration.ProjectConfiguration;

import java.util.Arrays;

public class InfoRetriever {

    private String name;
    private ProjectConfiguration[] projectConfigurations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Rush{" +
                "name='" + name + '\'' +
                ", projects=" + Arrays.toString(projectConfigurations) +
                '}';
    }

    public ProjectConfiguration[] getProjects() {
        return projectConfigurations;
    }

    public void setProjects(ProjectConfiguration[] projectConfigurations) {
        this.projectConfigurations = projectConfigurations;
    }

}

