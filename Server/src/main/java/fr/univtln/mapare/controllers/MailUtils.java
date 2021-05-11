package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public abstract class MailUtils {
    static final String SERVER_EMAIL_ADDRESS = "maparevote@gmail.com";
    static final String SERVER_PASSWORD = "alfrni666";

    private MailUtils() {}

    public static void sendConfirmationMail(User user) {
        int port = 465;
        Properties properties = new Properties();
        properties.put("mail.smtp.user", SERVER_EMAIL_ADDRESS);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.debug", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl.checkserveridentity", "true");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SERVER_EMAIL_ADDRESS, SERVER_PASSWORD);
            }
        });

        session.setDebug(true);

        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setSubject("Confirmation code");
            msg.setFrom(new InternetAddress(SERVER_EMAIL_ADDRESS));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            msg.setText(user.getEmailToken());

            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", port, SERVER_EMAIL_ADDRESS, SERVER_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static class RunnableMailSending implements Runnable {
        private final User user;

        public RunnableMailSending(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            sendConfirmationMail(user);
        }
    }

    public static RunnableMailSending runnableFor(User user) {
        return new RunnableMailSending(user);
    }
}
