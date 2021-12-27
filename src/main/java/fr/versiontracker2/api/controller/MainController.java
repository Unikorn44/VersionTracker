package fr.versiontracker2.api.controller;

import fr.versiontracker2.api.mapper.Mapper;
import fr.versiontracker2.api.ressource.FileApplication;
import fr.versiontracker2.api.ressource.MailContact;
import fr.versiontracker2.traitement.service.ApplicationService;
import fr.versiontracker2.transverse.exception.NonReadableApplicationConfigurationException;
import fr.versiontracker2.transverse.exception.NonReadableDependencyFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping("triApplication")
    public String triApplication(Model model, @RegisteredOAuth2AuthorizedClient("login-client") OAuth2AuthorizedClient authorizedClient) throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication(authorizedClient));
        model.addAttribute(LIST_FILE_APPLICATIONS, listFileApplications);

        return "triApplication";
    }

    @GetMapping("triProject")
    public String triProject(Model model, @RegisteredOAuth2AuthorizedClient("login-client") OAuth2AuthorizedClient authorizedClient) throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

        List<FileApplication> listFileApplications = mapper.transformeModeleEnRessource(applicationService.getInfoApplication(authorizedClient));

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

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }

    @PostMapping(value = "/mailContact", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE} )
    public String contact(MailContact mailContact) {
        System.out.println(mailContact);
        return "index";
    }
}
