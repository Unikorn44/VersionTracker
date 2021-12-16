package fr.versiontracker.api.controller;

import fr.versiontracker.api.mapper.Mapper;
import fr.versiontracker.api.ressource.FileApplication;
import fr.versiontracker.traitement.service.ApplicationService;
import fr.versiontracker.transverse.exception.NonReadableApplicationConfigurationException;
import fr.versiontracker.transverse.exception.NonReadableDependencyFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    public static final String LIST_FILE_APPLICATIONS = "listFileApplications";

    @Autowired
    ApplicationService applicationService;

    @Autowired
    Mapper mapper;

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication());
        model.addAttribute(LIST_FILE_APPLICATIONS, listFileApplications);

        return "index";
    }

    @RequestMapping(value = {"triApplication"})
    public String triApplication(Model model) throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication());
        model.addAttribute(LIST_FILE_APPLICATIONS, listFileApplications);

        return "triApplication";
    }

    @RequestMapping(value = {"triProject"})
    public String triProject(Model model) throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication());

        Map<String,Map<String,Map<String,String>>> listDependencies = new HashMap<>();

        listFileApplications.forEach(a->
            a.getListProjets().forEach(p ->
                p.getTrackedDependencyInfos().forEach(d->{
                    if(listDependencies.containsKey(p.getName())) {
                        if (!listDependencies.get(p.getName()).containsKey(d.getDependency())) {
                            Map<String,String> projetVersion = new HashMap<>();
                            projetVersion.put(a.getFileApplicationName(), d.getVersion());
                            listDependencies.get(p.getName()).put(d.getDependency(), projetVersion);
                        }else{
                            listDependencies.get(p.getName()).get(d.getDependency())
                                    .put(a.getFileApplicationName(), d.getVersion());
                        }
                    }else{
                        Map<String,Map<String,String>> listDependenciesProjects = new HashMap<>();
                        Map<String,String> projetVersion = new HashMap<>();
                        projetVersion.put(a.getFileApplicationName(), d.getVersion());
                        listDependenciesProjects.put(d.getDependency(), projetVersion);
                        listDependencies.put(p.getName(), listDependenciesProjects);
                    }
                })
            )
        );

        model.addAttribute(LIST_FILE_APPLICATIONS, listFileApplications);
        model.addAttribute("listDependencies", listDependencies);

        return "triProject";
    }

    @RequestMapping(value = {"/contact"})
    public String contact(Model model) {
        return "contact";
    }

    private WebClient webClient;

}
