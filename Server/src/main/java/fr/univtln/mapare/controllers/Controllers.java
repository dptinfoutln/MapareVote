package fr.univtln.mapare.controllers;

import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


/**
 * The type Controllers.
 */
public class Controllers {
    private static EntityManagerFactory eMF = null;
    private static EntityManager entityManager = null;

    private Controllers() {
    }


    /**
     * Gets entity manager.
     *
     * @return the entity manager
     */
    public static EntityManager getEntityManager() {

        entityManager.setProperty("LABEL", "%");
        return entityManager;
    }


    /**
     * Gets emf.
     *
     * @return the emf
     */
    public static EntityManagerFactory getEMF() {
        if (eMF != null)
            return eMF;
        else
            throw new IllegalStateException("EMF uninitialized.");
    }

    /**
     * Is open boolean.
     *
     * @return the boolean
     */
    public static boolean isOpen() {
        return entityManager != null;
    }

    /**
     * Init.
     */
    public static void init() {
        _init("maparevotedb");
    }

    /**
     * Testinit.
     */
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

    /**
     * Close.
     */
    public static void close() {
        entityManager.close();
        entityManager = null;
    }

    /**
     * Check user.
     *
     * @param user the user
     * @throws ForbiddenException the forbidden exception
     */
    public static void checkUser(User user) throws ForbiddenException {
        if (!user.isConfirmed())
            throw new ForbiddenException("You need to confirm your email first.");
        if (user.isBanned())
            throw new ForbiddenException("User is banned.");
    }
}
