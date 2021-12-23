package fr.versiontracker2.traitement.modele;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MavenDependency {

    private String groupId;
    private String artifactId;

}
