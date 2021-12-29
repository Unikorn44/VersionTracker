package fr.versiontracker2.traitement.modele;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ProjectConfiguration {

    private String name;
    private String fileUrl;
    private String packageManager;
    private List<String> trackedDependencies;

    @JsonIgnore
    private List<Dependency> dependencies;

}

