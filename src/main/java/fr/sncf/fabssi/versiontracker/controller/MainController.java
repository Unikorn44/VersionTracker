package fr.sncf.fabssi.versiontracker.controller;

import fr.sncf.fabssi.versiontracker.application.FileApplication;
import fr.sncf.fabssi.versiontracker.application.Projet;
import fr.sncf.fabssi.versiontracker.application.GetProjectsForApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    public static final String FRONTEND = "frontend";
    public static final String BACKEND = "backend";
    private List<Projet> listProjects;
    private List<FileApplication> listFileApplications;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) throws Exception {

        listFileApplications = GetProjectsForApplication.getInfoApplication();
        //System.out.println("Da list :" + listProjects);System.out.println("Da list :" + listProjects);
        model.addAttribute("listFileApplications", listFileApplications);

        return "index";
    }

    @RequestMapping(value = {"triApplication"}, method = RequestMethod.GET)
    public String triApplication(Model model) throws Exception {

        /*listProjects = GetProjectsForApplication.getInfoApplication();
        //System.out.println("Da list :" + listProjects);
        model.addAttribute("listProjects", listProjects);*/

        listFileApplications = GetProjectsForApplication.getInfoApplication();
        //System.out.println("Da list :" + listProjects);System.out.println("Da list :" + listProjects);
        model.addAttribute("listFileApplications", listFileApplications);

        /*
        List<String> testList = List.of("Élément 1", "Élément 2", "Un autre élément");
        String uneVariable = "Ceci est une variable";
        model.addAttribute("uneVariable", uneVariable);
        model.addAttribute("testList", testList);
        */

        return "triApplication";
    }

    @RequestMapping(value = {"triProject"}, method = RequestMethod.GET)
    public String triProject(Model model) throws Exception {

        listFileApplications = GetProjectsForApplication.getInfoApplication();

        Map<String,Map<String,Map<String,String>>> listDependencies = new HashMap<>();

        listFileApplications.forEach(a->{
            a.getListProjets().forEach(p -> {
                p.getDependencyInfos().forEach(d->{
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
