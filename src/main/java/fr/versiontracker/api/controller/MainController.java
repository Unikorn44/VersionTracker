package fr.versiontracker.api.controller;

import fr.versiontracker.api.mapper.Mapper;
import fr.versiontracker.api.ressource.FileApplication;
import fr.versiontracker.traitement.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    ApplicationService applicationService;

    @Autowired
    Mapper mapper;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) throws Exception {

        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication());
        model.addAttribute("listFileApplications", listFileApplications);

        return "index";
    }

    @RequestMapping(value = {"triApplication"}, method = RequestMethod.GET)
    public String triApplication(Model model) throws Exception {


        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication());
        model.addAttribute("listFileApplications", listFileApplications);

        return "triApplication";
    }

    @RequestMapping(value = {"triProject"}, method = RequestMethod.GET)
    public String triProject(Model model) throws Exception {

        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication());

        Map<String,Map<String,Map<String,String>>> listDependencies = new HashMap<>();

        listFileApplications.forEach(a->{
            a.getListProjets().forEach(p -> {
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
                });
            });
        });

        model.addAttribute("listFileApplications", listFileApplications);
        model.addAttribute("listDependencies", listDependencies);

        return "triProject";
    }

    @RequestMapping(value = {"/contact"}, method = RequestMethod.GET)
    public String contact(Model model) throws Exception {
        return "contact";
    }

}
