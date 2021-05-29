package fr.univtln.mapare.resources;

import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.security.LoginModule;
import fr.univtln.mapare.security.annotations.BasicAuth;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.java.Log;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Resource Class for authentication and signing into an account.
 */
@Log
@Path("auth")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class AuthResource {
    /**
     * a GET method to obtain a JWT token with basic authentication.
     *
     * @param securityContext the security context
     * @return the base64 encoded JWT Token.
     * @throws ForbiddenException the forbidden exception
     */
    @GET
    @Path("signin")
    @RolesAllowed({"USER", "ADMIN"})
    @BasicAuth
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public String login(@Context SecurityContext securityContext) throws ForbiddenException {
        if (securityContext.isSecure() && securityContext.getUserPrincipal() instanceof User) {
            User user = (User) securityContext.getUserPrincipal();

            if (!user.isConfirmed())
                throw new ForbiddenException("You need to validate your email first.");

            return Jwts.builder()
                    .setIssuer("MapareVote")
                    .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                    .setSubject(user.getEmail())
                    .claim("firstname", user.getFirstname())
                    .claim("lastname", user.getLastname())
                    .setExpiration(Date.from(LocalDateTime.now().plus(1, ChronoUnit.YEARS).atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(LoginModule.KEY).compact();
        }
        throw new WebApplicationException(new AuthenticationException());
    }
}
