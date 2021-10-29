package fr.versiontracker.traitement.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationConfiguration {

    private String name;
    @JsonProperty("projects") //lecture de la balise projects dans un attribut projectConfigurations
    private List<ProjectConfiguration> projectConfigurations;

    @JsonIgnore
    private String fileApplicationName;

}

