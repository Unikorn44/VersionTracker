package fr.versiontracker2.traitement.modele;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApplicationConfiguration {

    @JsonIgnore
    private String fileApplicationName;

    private String name;
    @JsonProperty("projects") //lecture de la balise projects dans un attribut projectConfigurations
    private List<ProjectConfiguration> projectConfigurations;

}

