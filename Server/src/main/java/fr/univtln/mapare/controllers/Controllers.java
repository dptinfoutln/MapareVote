package fr.univtln.mapare.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Controllers {
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");
    private static final EntityManagerFactory TestEMF = Persistence.createEntityManagerFactory("maparevotedev");
    private static EntityManager entityManager = null;

    private Controllers() {}

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static boolean isOpen() {
        return entityManager != null;
    }

    public static void init() {
        if (entityManager == null)
            entityManager = EMF.createEntityManager();
    }

    public static void testinit() {
        if (entityManager == null)
            entityManager = TestEMF.createEntityManager();
    }

    public static void close() {
        entityManager.close();
        entityManager = null;
    }
}
