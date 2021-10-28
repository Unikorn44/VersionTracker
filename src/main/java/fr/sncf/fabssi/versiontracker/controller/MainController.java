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

        Map<String,Map<String,String>> listDependenciesFront = new HashMap<>();
        Map<String,Map<String,String>> listDependenciesBack = new HashMap<>();

        listFileApplications.forEach(a->{
            a.getListProjets().forEach(p -> {
                p.getDependencyInfos().forEach(d->{
                    if(FRONTEND.equals(p.getName())) {
                        if (!listDependenciesFront.containsKey(d.getDependency())) {
                            Map<String,String> projetVersion = new HashMap<>();
                            projetVersion.put(a.getFileApplicationName(), d.getVersion());
                            listDependenciesFront.put(d.getDependency(), projetVersion);
                        }else{
                            listDependenciesFront.get(d.getDependency())
                                    .put(a.getFileApplicationName(), d.getVersion());
                        }
                    }else if(BACKEND.equals(p.getName())) {
                        if (!listDependenciesBack.containsKey(d.getDependency())) {
                            Map<String,String> projetVersion = new HashMap<>();
                            projetVersion.put(a.getFileApplicationName(), d.getVersion());
                            listDependenciesBack.put(d.getDependency(), projetVersion);
                        }else{
                            listDependenciesBack.get(d.getDependency())
                                    .put(a.getFileApplicationName(), d.getVersion());
                        }
                    }
                });
            });
        });
        /*System.out.println("Da list Front:" + listDependenciesFront);
        System.out.println("Da list Back :" + listDependenciesBack);*/

        model.addAttribute("listFileApplications", listFileApplications);
        model.addAttribute("listDependenciesFront", listDependenciesFront);
        model.addAttribute("listDependenciesBack", listDependenciesBack);

        return "triProject";
    }

    @RequestMapping(value = {"/contact"}, method = RequestMethod.GET)
    public String contact(Model model) throws Exception {
        return "contact";
    }

}
