package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * The type Mail utils.
 */
public abstract class MailUtils {
    /**
     * The Server email address.
     */
    static final String SERVER_EMAIL_ADDRESS = "maparevote@gmail.com";
    /**
     * The Server password.
     */
    static final String SERVER_PASSWORD = "alfrni666";
    /**
     * The Auth link.
     */
    static final String AUTH_LINK = "https://maparevote.siannos.fr/validate/";
    /**
     * The Voting link.
     */
    static final String VOTING_LINK = "https://maparevote.siannos.fr/votes/";

    private MailUtils() {
    }

    /**
     * Send confirmation mail.
     *
     * @param user the user
     */
    public static void sendConfirmationMail(User user) {
        sendMail("[MapareVote] Code de confirmation!", user,
                "Bonjour " + user.getFirstname() + " " + user.getLastname() + "!\n\nVous vous êtes " +
                        "inscrit à MapareVote dans le but de faire connaître votre opinion et d'interroger celle " +
                        "des autres sur des sujets divers et variés.\n\n\n" +
                        "Pour valider votre compte il suffit de cliquer sur le lien suivant: " + AUTH_LINK + user.getId() +
                        "/" + user.getEmailToken() + "\n\n\nSi ce n'est pas le cas, merci d'ignorer ce mail.");
    }

    /**
     * Send invitation mail.
     *
     * @param vote    the vote
     * @param invitee the invitee
     */
    public static void sendInvitationMail(Vote vote, User invitee) {
        sendMail("[MapareVote] Invitation à un vote!", invitee,
                "Bonjour " + invitee.getFirstname() + " " + invitee.getLastname() + "!\n\n" +
                        "Vous avez été invité par " + vote.getVotemaker().getFirstname() + " " +
                        vote.getVotemaker().getLastname() + " à participer a un vote privé intitulé: \"" + vote.getLabel() +
                        "\" !\n\nPour participer il vous suffit de cliquer sur ce lien: " + VOTING_LINK + vote.getId() + ".");
    }

    /**
     * Send mail.
     *
     * @param title   the title
     * @param user    the user
     * @param content the content
     */
    public static void sendMail(String title, User user, String content) {
        int port = 465;
        Properties properties = new Properties();
        properties.put("mail.smtp.user", SERVER_EMAIL_ADDRESS);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", "true");
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
            msg.setSubject(title);
            msg.setFrom(new InternetAddress(SERVER_EMAIL_ADDRESS));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            msg.setText(content);
            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", port, SERVER_EMAIL_ADDRESS, SERVER_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * The type Confirmation mail sender.
     */
    public static class ConfirmationMailSender implements Runnable {
        private final User user;

        /**
         * Instantiates a new Confirmation mail sender.
         *
         * @param user the user
         */
        public ConfirmationMailSender(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            sendConfirmationMail(user);
        }
    }

    /**
     * Send confirmation to confirmation mail sender.
     *
     * @param user the user
     * @return the confirmation mail sender
     */
    public static ConfirmationMailSender sendConfirmationTo(User user) {
        return new ConfirmationMailSender(user);
    }

    /**
     * The type Invitation mail sender.
     */
    public static class InvitationMailSender implements Runnable {
        private final User user;

        /**
         * Instantiates a new Invitation mail sender.
         *
         * @param user the user
         */
        public InvitationMailSender(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            sendConfirmationMail(user);
        }
    }

    /**
     * Send invitation to invitation mail sender.
     *
     * @param user the user
     * @return the invitation mail sender
     */
    public static InvitationMailSender sendInvitationTo(User user) {
        return new InvitationMailSender(user);
    }
}
