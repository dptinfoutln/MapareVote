package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.security.annotations.JWTAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("users")
public class UserResource {

    @GET
    public List<User> getUsers(@DefaultValue("1") @QueryParam("page_num") int pagenum,
                         @DefaultValue("20") @QueryParam("page_size") int pagesize) {
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
    public User addUser(User user) {
        user.setId(0);
        user.setVotedVotes(null);
        user.setPrivateVoteList(null);
        user.setStartedVotes(null);
        user.setConfirmed(false);
        user.setAdmin(false);
        user.setBanned(false);
        UserDAO.of(Controllers.getEntityManager()).persist(user);
        return user;
    }

    @GET
    @Path("{uid}/votedvotes/{vid}/ballot") // TODO: move to votes/id/myballot
    public Ballot getSpecificBallotforUser(@PathParam("uid") int uid, @PathParam("vid") int vid) {
        if (!VoteDAO.of(Controllers.getEntityManager()).findById(vid).getAnonymous()) {
            return BallotDAO.of(Controllers.getEntityManager()).findByVoteByVoter(
                    VoteDAO.of(Controllers.getEntityManager()).findById(vid),
                    UserDAO.of(Controllers.getEntityManager()).findById(uid)
            );
        }
        else
            return null;
    }
}
