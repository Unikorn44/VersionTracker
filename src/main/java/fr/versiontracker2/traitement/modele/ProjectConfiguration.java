package fr.versiontracker.traitement.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ProjectConfiguration {

    private String name;
    private String fileUrl;
    private String packageManager;
    private List<String> trackedDependencies;

    @JsonIgnore
    private List<Dependency> dependencies;

}

