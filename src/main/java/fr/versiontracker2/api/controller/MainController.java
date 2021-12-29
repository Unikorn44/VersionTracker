package fr.versiontracker2.api.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import fr.versiontracker2.api.mapper.Mapper;
import fr.versiontracker2.api.ressource.FileApplication;
import fr.versiontracker2.api.ressource.MailContact;
import fr.versiontracker2.traitement.service.ApplicationService;
import fr.versiontracker2.transverse.exception.NonReadableApplicationConfigurationException;
import fr.versiontracker2.transverse.exception.NonReadableDependencyFileException;

@Controller
public class MainController {

	public static final String LIST_FILE_APPLICATIONS = "listFileApplications";

	@Autowired
	ApplicationService applicationService;

	@Autowired
	Mapper mapper;

	@GetMapping(value = { "/", "/index" })
	public String index(Model model) {
		return "index";
	}

	@GetMapping("/triApplication")
	public String triApplication(Model model,
			@RegisteredOAuth2AuthorizedClient("login-client") OAuth2AuthorizedClient authorizedClient)
			throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

		List<FileApplication> listFileApplications = mapper
				.transformeModeleEnRessource(applicationService.getInfoApplication(authorizedClient));
		model.addAttribute(LIST_FILE_APPLICATIONS, listFileApplications);

		return "triApplication";
	}

	@GetMapping("/triProject")
	public String triProject(Model model,
			@RegisteredOAuth2AuthorizedClient("login-client") OAuth2AuthorizedClient authorizedClient)
			throws NonReadableApplicationConfigurationException, NonReadableDependencyFileException {

		List<FileApplication> listFileApplications = mapper
				.transformeModeleEnRessource(applicationService.getInfoApplication(authorizedClient));

		Map<String, Map<String, Map<String, String>>> listDependencies = new HashMap<>();

		listFileApplications.forEach(a -> a.getListProjets().forEach(p -> p.getTrackedDependencyInfos().forEach(d -> {
			if (listDependencies.containsKey(p.getName())) {
				if (!listDependencies.get(p.getName()).containsKey(d.getDependency())) {
					Map<String, String> projetVersion = new HashMap<>();
					projetVersion.put(a.getFileApplicationName(), d.getVersion());
					listDependencies.get(p.getName()).put(d.getDependency(), projetVersion);
				} else {
					listDependencies.get(p.getName()).get(d.getDependency()).put(a.getFileApplicationName(),
							d.getVersion());
				}
			} else {
				Map<String, Map<String, String>> listDependenciesProjects = new HashMap<>();
				Map<String, String> projetVersion = new HashMap<>();
				projetVersion.put(a.getFileApplicationName(), d.getVersion());
				listDependenciesProjects.put(d.getDependency(), projetVersion);
				listDependencies.put(p.getName(), listDependenciesProjects);
			}
		})));

		model.addAttribute(LIST_FILE_APPLICATIONS, listFileApplications);
		model.addAttribute("listDependencies", listDependencies);

		return "triProject";
	}

	@GetMapping("/contact")
	public String contact(Model model,
			@RegisteredOAuth2AuthorizedClient("login-client") OAuth2AuthorizedClient authorizedClient) {
		return "contact";
	}

	@PostMapping(value = "/mailContact", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public String contact(MailContact mailContact,
			@RegisteredOAuth2AuthorizedClient("login-client") OAuth2AuthorizedClient authorizedClient) {
		String skinner = authorizedClient.getPrincipalName();
		System.out.println(mailContact);
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp-in.orange.fr");

		Session session = Session.getInstance(props);
		MimeMessage mail = new MimeMessage(session);

		try {
			mail.setContent("<html><body><h1>Message de " + mailContact.getName() + " (" + mailContact.getEmail() + ")"
					+ "</h1><br/><p>" + mailContact.getMessage() + "</p></body></html>", "text/html");
			mail.setSubject("Un message a été laissé sur Version Tracker par " + skinner);
			InternetAddress[] recipients = new InternetAddress[1];
			recipients[0] = new InternetAddress("phmel@orange.fr");
			mail.setRecipients(Message.RecipientType.TO, recipients);
			mail.setHeader("X-Mailer", "Java");
			mail.setSentDate(new Date());
			mail.setSender(new InternetAddress("phmel@orange.fr"));
			mail.setFrom(new InternetAddress("phmel@orange.fr"));
			InternetAddress[] replyto = new InternetAddress[1];
			replyto[0] = new InternetAddress(mailContact.getEmail());
			mail.setReplyTo(replyto);
			Transport.send(mail);
		} catch (MessagingException e) {
			// TODO Gérer proprement l'exception
			e.printStackTrace();
		}
		return "index";
	}
}
