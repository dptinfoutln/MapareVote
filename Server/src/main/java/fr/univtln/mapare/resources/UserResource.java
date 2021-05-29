package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.controllers.MailUtils;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.exceptions.NotFoundException;
import fr.univtln.mapare.exceptions.*;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.security.annotations.JWTAuth;
import jakarta.persistence.RollbackException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

/**
 * The type User resource.
 */
@Path("users")
public class UserResource {
    /**
     * Gets users.
     *
     * @param securityContext the security context
     * @return the users
     * @throws ForbiddenException the forbidden exception
     */
    @GET
    @JWTAuth
    public List<User> getUsers(@Context SecurityContext securityContext) throws ForbiddenException {
        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        if (!user.isAdmin())
            throw new ForbiddenException("You do not have the rights to do this.");

        return UserDAO.of(Controllers.getEntityManager()).findAll();
    }

    /**
     * Gets user.
     *
     * @param id the id
     * @return the user
     */
    @GET
    @JWTAuth
    @Path("{id}")
    public User getUser(@Context SecurityContext securityContext,
                        @PathParam("id") int id) throws ForbiddenException {
        User user = (User) securityContext.getUserPrincipal();

        if (!user.isAdmin())
            throw new ForbiddenException("You do not have the rights to do this.");

        return UserDAO.of(Controllers.getEntityManager()).findById(id);
    }

    /**
     * Gets self.
     *
     * @param securityContext the security context
     * @return the self
     */
    @GET
    @JWTAuth
    @Path("me")
    public User getSelf(@Context SecurityContext securityContext) {
        return (User) securityContext.getUserPrincipal();
    }

    /**
     * Add user user.
     *
     * @param user the user
     * @return the user
     * @throws BusinessException the business exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User addUser(User user) throws BusinessException {
        user.setId(0);
        user.setVotedVotes(null);
        user.setPrivateVoteList(null);
        user.setStartedVotes(null);
        user.setConfirmed(false);
        user.setAdmin(false);
        user.setBanned(false);

        if (user.getEmail() == null || user.getFirstname() == null || user.getLastname() == null
                || user.getPasswordHash() == null || user.getSalt() == null)
            throw new ForbiddenException("Error: some fields haven't been filled.");

        try {
            UserDAO.of(Controllers.getEntityManager()).persist(user);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        } catch (RollbackException re) {
            throw new ConflictException("Email already in use.");
        }

//        MailUtils.sendConfirmationMail(user);
        new Thread(MailUtils.sendConfirmationTo(user)).start();

        return user;
    }

    /**
     * Delete user int.
     *
     * @param securityContext the security context
     * @param id              the id
     * @return the int
     * @throws NotFoundException     the not found exception
     * @throws UnauthorizedException the unauthorized exception
     * @throws ForbiddenException    the forbidden exception
     */
    @DELETE
    @JWTAuth
    @Path("{id}")
    public int deleteUser(@Context SecurityContext securityContext,
                          @PathParam("id") int id) throws NotFoundException, UnauthorizedException, ForbiddenException {
        User currUser = (User) securityContext.getUserPrincipal();

        if (currUser == null)
            throw new UnauthorizedException("Please login.");

        if (!currUser.isConfirmed())
            throw new ForbiddenException("You need to confirm your email first.");

        if (currUser.getId() != id && !currUser.isAdmin())
            throw new ForbiddenException("You do not have the rights to do this.");

        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
        if (dao.findById(id) != null) {
            dao.remove(id);
        } else
            throw new NotFoundException("Trying to delete user that doesn't exist.");
        return 0;
    }

    /**
     * Ban user int.
     *
     * @param securityContext the security context
     * @param id              the id
     * @return the int
     * @throws ForbiddenException    the forbidden exception
     * @throws NotFoundException     the not found exception
     * @throws UnauthorizedException the unauthorized exception
     */
    @JWTAuth
    @PATCH
    @Path("{id}/ban")
    public int banUser(@Context SecurityContext securityContext,
                       @PathParam("id") int id) throws ForbiddenException, NotFoundException, UnauthorizedException {
        User currUser = (User) securityContext.getUserPrincipal();

        if (currUser == null)
            throw new UnauthorizedException("Please authenticate yourself.");

        if (!currUser.isAdmin())
            throw new ForbiddenException("You do not have the rights for this.");

        if (!currUser.isConfirmed())
            throw new ForbiddenException("You need to validate your email first.");

        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
        User toBan = dao.findById(id);

        if (toBan == null)
            throw new NotFoundException("User not found.");

        toBan.setBanned(true);

        dao.update(toBan);

        return 0;
    }

    /**
     * Validate user string.
     *
     * @param id    the id
     * @param token the token
     * @return the string
     * @throws NotFoundException the not found exception
     * @throws ConflictException the conflict exception
     */
    @GET
    @Path("{id}/validate/{token}")
    public String validateUser(@PathParam("id") int id, @PathParam("token") String token)
            throws NotFoundException, ConflictException {
        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
        User user = dao.findById(id);
        System.out.println(user);
        if (user == null)
            throw new NotFoundException("User not found.");
        if (!user.isConfirmed()) {
            if (!user.getEmailToken().equals(token))
                throw new ConflictException("Token does not match.");
            user.setConfirmed(true);
            dao.update(user);
            return "Ok";
        } else {
            throw new ConflictException("User is already confirmed");
        }
    }
}
