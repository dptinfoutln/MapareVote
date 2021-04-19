package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class UserDAO extends DAO<User> {

    public static UserDAO of(EntityManager entityManager) {
        return new UserDAO(entityManager);
    }

    private UserDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    List<User> findAll() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    public User findByEmail(String email) {
        List<User> userList  = entityManager.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getResultList();
        return userList.isEmpty() ? null : userList.get(0);
    }






}
