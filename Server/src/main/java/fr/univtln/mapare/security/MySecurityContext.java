package fr.univtln.mapare.security;

import fr.univtln.mapare.security.filter.request.BasicAuthenticationFilter;
import fr.univtln.mapare.security.filter.request.JsonWebTokenFilter;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.security.Principal;


/**
 * This class define a specific security context after an authentication with either the basic or the JWT filters.
 *
 * @see BasicAuthenticationFilter
 * @see JsonWebTokenFilter
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "newInstance")
public class MySecurityContext implements SecurityContext {
    private final String authenticationScheme;
    private final String username;

    //the authenticated user
    @Override
    public Principal getUserPrincipal() {
        return LoginModule.getUser(username);
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    //Say the access has been secured
    @Override
    public boolean isSecure() {
        return true;
    }

    //The authentication scheme (Basic, JWT, ...) that has been used.
    @Override
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
}
