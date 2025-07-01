package common;


import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;

public class MailSender {

    public static String sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "systembpark@gmail.com";
        final String password = "exui mhqz xvax psdq"; // לא סיסמה רגילה – צריך סיסמת אפליקציה

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
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
