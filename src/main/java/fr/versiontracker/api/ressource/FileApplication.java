package fr.versiontracker.api.ressource;

import lombok.Data;

import java.util.List;

@Data
public class FileApplication {

    public String fileApplicationName;
    public List<Projet> listProjets;

}
