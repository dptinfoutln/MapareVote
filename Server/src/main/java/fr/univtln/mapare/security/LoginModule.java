package fr.univtln.mapare.security;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.resources.UserResource;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.logging.Logger;

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
     */
    public void addUser(String firstname, String lastname, String email, String password) {
        User user = User.builder().firstname(firstname).lastname(lastname).email(email).password(password).build();
        UserDAO.of(Controllers.getEntityManager()).persist(user);
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return (List<User>) Controllers.executeRequest("User.findAll");
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
        List<?> result = Controllers.executeParamRequest("User.findByEmail", "email", email);
        if (result.isEmpty())
            return null;
        else
            return (User) result.get(0);
    }

    @SuppressWarnings("SameReturnValue")
    public boolean logout() {
        return false;
    }

}
