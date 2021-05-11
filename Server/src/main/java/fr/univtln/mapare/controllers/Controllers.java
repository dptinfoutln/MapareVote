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
}
