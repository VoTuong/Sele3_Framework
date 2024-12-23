package org.tvd.utilities;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class EmailUtils {

	public static void sendEmail(String recipient, String subject, String body) {
		String reportPath;

		// Check for Allure report first, fallback to HTML if unavailable
		if (Files.exists(Paths.get("target/allure-report/index.html"))) {
			reportPath = "target/allure-report/index.html";
		} else if (Files.exists(Paths.get("test-output/emailable-report.html"))) {
			reportPath = "test-output/emailable-report.html";
		} else {
			throw new RuntimeException("No report found to attach.");
		}

		sendEmail(recipient, subject, body, reportPath);
	}


	public static void sendEmail(String to, String subject, String body, String reportPath) {
		String userName = "voddangtuon@gmail.com";
		String password = "adidaphat0K?";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.user", userName);
		props.put("mail.password", password);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(props, auth);

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);

			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(body);

			MimeBodyPart attachmentPart = new MimeBodyPart();
			attachmentPart.attachFile(reportPath);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(attachmentPart);

			message.setContent(multipart);

			Transport.send(message);
			System.out.println("Sent message successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
