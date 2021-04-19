package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("users")
public class UserResource {
//    static Controller<User> ctrl = new Controller<>();

    @GET
    public List<User> getUsers(@DefaultValue("1") @QueryParam("page_num") int pagenum,
                         @DefaultValue("20") @QueryParam("page_size") int pagesize) {
        //Lancer DAO
        //Pagination
        //rentrer users dans liste
        //ctrl.listAdd(new User(1, "test@example.com", "Dupont", "Thomas", "TESTX5", true, true, false));
//        return Controllers.Users.getList();
        return UserDAO.of(Controllers.getEntityManager()).findAll();
    }

    @GET
    @Path("{id}")
    public User getUser(@PathParam("id") int id) {
//        return Controllers.Users.mapGet(id);
        return UserDAO.of(Controllers.getEntityManager()).findById(id);
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
        //user.setEmailToken("inserttokenhere");
        UserDAO.of(Controllers.getEntityManager()).persist(user);
        return user;
    }

    @GET
    @Path("{uid}/votedvotes/{vid}/ballot")
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
