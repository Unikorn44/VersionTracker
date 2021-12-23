package fr.versiontracker2.traitement.modele;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NPMDependency {

    private String name;
    private String version;
    private Map<String, String> dependencies;

}

