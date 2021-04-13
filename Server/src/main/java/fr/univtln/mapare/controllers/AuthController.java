package fr.univtln.mapare.controllers;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AuthController {
    private static final List<String> TOKENS = new ArrayList<>();

    public static String signIn(String email, String hashedPass){
        //TODO: Generate Token
        TOKENS.add("123token123");
        return "123token123";
    }

    public static void signUp(String email, String password) {
        //TODO: Create new user
    }

    public static void signOut(String token) {
        TOKENS.remove(token);
    }
}
