package network;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Utility class for sending email messages using SMTP via Gmail. This class
 * uses the Jakarta Mail API to send simple text-based emails from a predefined
 * system address.
 */
public class MailSender {
	/**
	 * Sends an email to the specified recipient with the given subject and message
	 * body.
	 *
	 * @param toEmail the recipient's email address
	 * @param subject the subject line of the email
	 * @param body    the content/body of the email
	 * @return a status message indicating success or failure
	 */

	public static String sendEmail(String toEmail, String subject, String body) {
		final String fromEmail = "systembpark@gmail.com";
		final String password = "exui mhqz xvax psdq";
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new Authenticator() {
			/**
			 * Provides authentication credentials for the SMTP session.
			 * 
			 * @return password authentication for the sender email account
			 */
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
			System.out.println("Email sent successfully");
			return "Email sent successfully";

		} catch (MessagingException e) {
			e.printStackTrace();
			return "Failed to sent.";
		}
	}
}
