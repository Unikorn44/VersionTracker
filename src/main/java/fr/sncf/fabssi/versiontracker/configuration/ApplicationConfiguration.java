package fr.sncf.fabssi.versiontracker.configuration;

public class ApplicationConfiguration {

    private String name;
    private ProjectConfiguration[] projects;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectConfiguration[] getProjects() {
        return projects;
    }

    public void setProjects(ProjectConfiguration[] projectConfigurations) {
        this.projects = projectConfigurations;
    }
}
