package fr.sncf.fabssi.versiontracker.application;

import java.util.List;

public class FileApplication {

    public String fileApplicationName;
    public List<Projet> listProjets;

    @Override
    public String toString() {
        return "FileApplication {" +
                "fileApplicationName ='" + fileApplicationName + '\'' +
                ", liste projets'" + listProjets +
                '}';
    }

    public String getFileApplicationName() {
        return fileApplicationName;
    }

    public void setFileApplicationName(String fileApplicationName) {
        this.fileApplicationName = fileApplicationName;
    }

    public List<Projet> getListProjets() {
        return listProjets;
    }

    public void setListProjets(List<Projet> listProjets) {
        this.listProjets = listProjets;
    }
}
