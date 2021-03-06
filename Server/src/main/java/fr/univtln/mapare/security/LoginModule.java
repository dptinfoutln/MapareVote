package fr.univtln.mapare.security;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.model.User;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;

import java.security.Key;
import java.util.List;

/**
 * this class model a simple in memory role based authentication database (RBAC).
 * Password are salted and hashed.
 */
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Log
public class LoginModule {

    /**
     * The constant KEY is used as a signing key for the bearer JWT token.
     * It is used to check that the token hasn't been modified.
     */
    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Add user.
     *
     * @param firstname the firstname
     * @param lastname  the lastname
     * @param email     the email
     * @param password  the password
     * @throws BusinessException the business exception
     */
    public void addUser(String firstname, String lastname, String email, String password) throws BusinessException {
        User user = User.builder().firstname(firstname).lastname(lastname).email(email).password(password).build();
        UserDAO.of(Controllers.getEntityManager()).persist(user);
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public List<User> getUsers() {
        return UserDAO.of(Controllers.getEntityManager()).findAll();
    }

    /**
     * Remove user.
     *
     * @param email the email
     */
    public void removeUser(String email) {

    }

    /**
     * Check password boolean.
     *
     * @param email    the email
     * @param password the password
     * @return the boolean
     */
    public static boolean login(String email, String password) {
        User user = getUser(email);
        if (user != null)
            return user.checkPassword(password);
        else
            return false;
    }

    /**
     * Gets user.
     *
     * @param email the email
     * @return the user
     */
    public static User getUser(String email) {
        return UserDAO.of(Controllers.getEntityManager()).findByEmail(email);
    }

    /**
     * Logout boolean.
     *
     * @return the boolean
     */
    @SuppressWarnings("SameReturnValue")
    public boolean logout() {
        return false;
    }

}
