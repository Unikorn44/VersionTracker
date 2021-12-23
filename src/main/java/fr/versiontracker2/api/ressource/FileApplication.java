package fr.versiontracker2.api.ressource;

import lombok.Data;

import java.util.List;

@Data
public class FileApplication {

    private String fileApplicationName;
    private List<Projet> listProjets;

}
