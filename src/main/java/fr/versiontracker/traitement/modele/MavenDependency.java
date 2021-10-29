package fr.versiontracker.traitement.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MavenDependency {

    private String groupId;
    private String artifactId;

}
