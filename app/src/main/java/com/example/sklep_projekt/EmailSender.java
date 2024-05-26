package com.example.sklep_projekt;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    public static void sendEmailInBackground(final String recipient, final String subject, final String body) {
        new Thread(() -> {

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("radoslaw.wypij@gmail.com", "xwfz grno fmyt vlsf");
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("radoslaw.wypij@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
