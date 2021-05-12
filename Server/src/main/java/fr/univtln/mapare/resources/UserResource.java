package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.controllers.MailUtils;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.exceptions.*;
import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.exceptions.NotFoundException;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.security.annotations.JWTAuth;
import jakarta.persistence.RollbackException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("users")
public class UserResource {
    @GET
    public List<User> getUsers(@DefaultValue("1") @QueryParam("page_num") int pagenum,
                         @DefaultValue("20") @QueryParam("page_size") int pagesize) throws NotFoundException {
        // TODO: add admin authentication
        //Lancer DAO
        //Pagination
        //rentrer users dans liste
        return UserDAO.of(Controllers.getEntityManager()).findAll();
    }

    @GET
    @Path("{id}")
    public User getUser(@PathParam("id") int id) {
        return UserDAO.of(Controllers.getEntityManager()).findById(id);
    }

    @GET
    @JWTAuth
    @Path("me")
    public User getSelf(@Context SecurityContext securityContext) {
        return (User) securityContext.getUserPrincipal();
    }

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
        new Thread(MailUtils.runnableFor(user)).start();

        return user;
    }


    @DELETE
    @Path("{id}")
    public int deleteUser(@PathParam("id") int id) throws NotFoundException {
        //TODO: add authentication before deleting
        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
        if (dao.findById(id) != null) {
            dao.remove(id);
        } else
            throw new NotFoundException("Trying to delete user that doesn't exist.");
        return 0;
    }

//    @DELETE
//    @JWTAuth
//    @Path("{id}")
//    public int deleteUser(@Context SecurityContext securityContext,
//                          @PathParam("id") int id) throws NotFoundException, UnauthorizedException, ForbiddenException {
//        User currUser = (User) securityContext.getUserPrincipal();
//
//        if (currUser == null)
//            throw new UnauthorizedException("Please login.");
//
//        if (!currUser.isConfirmed())
//            throw new ForbiddenException("You need to confirm your email first.");
//
//        if (currUser.getId() != id && !currUser.isAdmin())
//            throw new ForbiddenException("You do not have the rights to do this.");
//
//        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
//        if (dao.findById(id) != null) {
//            dao.remove(id);
//        } else
//            throw new NotFoundException("Trying to delete user that doesn't exist.");
//        return 0;
//    }

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

    @GET
    @Path("{id}/validate/{token}")
    public String validateUser(@PathParam("id") int id, @PathParam("token") String token)
            throws NotFoundException, ConflictException {
        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
        User user = dao.findById(id);
        if (user == null)
            throw new NotFoundException("User not found.");
        if (!user.isConfirmed()) {
            if (!user.getEmailToken().equals(token))
                throw new ConflictException("Token does not match.");
            user.setConfirmed(true);
            dao.update(user);
        }
        return "Ok";
    }
}
