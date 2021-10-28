package fr.sncf.fabssi.versiontracker.configuration;

public class TrackedDependencyInfo {

    private String dependency; //rename to name
    private String version;

    @Override
    public String toString() {
        return "ProjectView {" +
                "Dependency='" + dependency + '\'' +
                ", version = " + version +
                '}';
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
