package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * The type User dao.
 */
public class UserDAO extends GenericIdDAO<User> {

    /**
     * Of user dao.
     *
     * @param entityManager the entity manager
     * @return the user dao
     */
    public static UserDAO of(EntityManager entityManager) {
        return new UserDAO(entityManager);
    }

    private UserDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<User> findAll() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    /**
     * Find all list.
     *
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the list
     */
    public List<User> findAll(int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("User.findAll", User.class)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();
    }

    /**
     * Find by email user.
     *
     * @param email the email
     * @return the user
     */
    public User findByEmail(String email) {
        List<User> userList = entityManager.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getResultList();
        return userList.isEmpty() ? null : userList.get(0);
    }
}
