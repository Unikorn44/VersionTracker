package fr.versiontracker2.traitement.modele;


import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NPMDependency {

    private String name;
    private String version;
    private Map<String, String> dependencies;

}

