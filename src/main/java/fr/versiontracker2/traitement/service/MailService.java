package fr.versiontracker2.traitement.service;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import fr.versiontracker2.api.resource.MailContact;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {

	private static final String PHMEL = "phmel@orange.fr";
	private static final String OWASP_MAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+"
			+ "(?:\\.[a-zA-Z0-9_+&*-]+){0,9}@(?:[a-zA-Z0-9-]+\\.){1,9}[a-zA-Z]{2,7}$";
	
	public void sendMail(MailContact mailContact, String username) {
		
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp-in.orange.fr");
		props.put("mail.smtp.connectionTimeout", 25000);
		Session session = Session.getInstance(props);
		MimeMessage mail = new MimeMessage(session);

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body><h1>Message de ");
			sb.append(escapeHtml4(mailContact.getName()));
			sb.append(" (");
			sb.append(escapeHtml4(mailContact.getEmail()));
			sb.append(")");
			sb.append("</h1><br/><p>");
			sb.append(escapeHtml4(mailContact.getMessage()));
			sb.append("</p></body></html>");
			mail.setContent( sb.toString() , "text/html");
			mail.setSubject("Un message a été laissé sur Version Tracker par " + username);
			InternetAddress[] recipients = new InternetAddress[1];
			recipients[0] = new InternetAddress(PHMEL);
			mail.setRecipients(Message.RecipientType.TO, recipients);
			mail.setHeader("X-Mailer", "Java");
			mail.setSentDate(new Date());
			mail.setSender(new InternetAddress(PHMEL));
			mail.setFrom(new InternetAddress(PHMEL));
			if (Pattern.compile(OWASP_MAIL_PATTERN).matcher(mailContact.getEmail()).matches()) {
				InternetAddress[] replyto = new InternetAddress[1];
				replyto[0] = new InternetAddress(mailContact.getEmail());
				mail.setReplyTo(replyto);
			}
			log.info("Envoi du mail de " + username);
			Transport.send(mail);
			log.info("Mail envoyé");
		} catch (MessagingException e) {
			log.error("Erreur à l'envoi du mail",e);
		}
	}
}
