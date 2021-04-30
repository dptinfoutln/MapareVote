package fr.univtln.mapare.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class Controllers {
    private static EntityManagerFactory EMF = null;
    private static EntityManagerFactory TestEMF = null;
    private static EntityManager entityManager = null;

    private Controllers() {}

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static boolean isOpen() {
        return entityManager != null;
    }

    public static void init() {
        EMF = Persistence.createEntityManagerFactory("maparevotedb");
        if (entityManager == null)
            entityManager = EMF.createEntityManager();
    }

    public static void testinit() {
        TestEMF = Persistence.createEntityManagerFactory("maparevotedev");
        if (entityManager == null)
            entityManager = TestEMF.createEntityManager();
    }

    public static void close() {
        entityManager.close();
        entityManager = null;
    }
}
