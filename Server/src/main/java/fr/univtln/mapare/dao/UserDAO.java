package fr.univtln.mapare.dao;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserDAO {
    private UserDAO() {}

    public static void persist(User user) {
        EntityManager entityManager = Controllers.getEntityManager();
        EntityTransaction trans = entityManager.getTransaction();
        trans.begin();
        entityManager.persist(user);
        entityManager.flush();
        trans.commit();
    }
}
