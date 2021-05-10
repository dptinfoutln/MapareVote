package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;


public class Controllers {
    private static EntityManagerFactory eMF = null;
    private static EntityManager entityManager = null;

    private Controllers() {}

    public static EntityManager getEntityManager() {

        entityManager.setProperty("LABEL", "%");
        return entityManager;
    }

    public static EntityManagerFactory getEMF() {
        if (eMF != null)
            return eMF;
        else
            throw new IllegalStateException("EMF uninitialized.");
    }

    public static boolean isOpen() {
        return entityManager != null;
    }

    public static void init() {
        _init("maparevotedb");
    }

    public static void testinit() {
        _init("maparevotedev");
    }

    private static void _init(String persistenceUnitName) {
        eMF = Persistence.createEntityManagerFactory(persistenceUnitName);
        if (entityManager == null)
            entityManager = eMF.createEntityManager();
        entityManager.setProperty("LABEL", "%");
        entityManager.setProperty("ALGO", "%");
    }

    public static void close() {
        entityManager.close();
        entityManager = null;
    }

    public static void sendConfirmationMail(User user) {
        String serverEmailAddress = "maparevote@gmail.com";
        int port = 465;
        Properties properties = new Properties();
        properties.put("mail.smtp.user", serverEmailAddress);
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
                return new PasswordAuthentication(serverEmailAddress, "alfrni666");
            }
        });

        session.setDebug(true);

        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setSubject("Confirmation code");
            msg.setFrom(new InternetAddress(serverEmailAddress));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            msg.setText(user.getEmailToken());

            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", port, serverEmailAddress, "alfrni666");
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendConfirmationMail(new User("francois-palma@etud.univ-tln.fr", "Palma", "François",
                "hello"));
    }
}
