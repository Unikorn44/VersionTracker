package fr.versiontracker2.api.ressource;

import java.util.List;

import lombok.Data;

@Data
public class FileApplication {

    private String fileApplicationName;
    private List<Projet> listProjets;

}
