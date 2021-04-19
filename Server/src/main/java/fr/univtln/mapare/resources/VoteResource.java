package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.*;

import java.time.LocalDate;
import java.util.*;

@Path("votes")
public class VoteResource {
//    static Controller<Vote> ctrl = new Controller<>();

    @GET
    @Path("public")
    public List<Vote> getVotes(@QueryParam("page_num") int pagenum,
                         @QueryParam("page_size") int pagesize) {
        return VoteDAO.of(Controllers.getEntityManager()).findAllPublic();
    }

    @GET
    @Path("{id}")
    public Vote getVote(@PathParam("id") int id) {
        //TODO: Check if the user can see this vote (memberlist, etc.)
        return VoteDAO.of(Controllers.getEntityManager()).findById(id);
    }

    @POST
    @Path("public")
    public Vote addPublicVote(Vote vote) {
        vote.setVotemaker(UserDAO.of(Controllers.getEntityManager()).findById(vote.getVotemaker().getId()));
        vote.setMembers(null);
        return addVote(vote);
    }

    @POST
    @Path("private")
    public Vote addPrivateVote(Vote vote) {
        User voteMaker = UserDAO.of(Controllers.getEntityManager()).findById(vote.getVotemaker().getId());
        vote.setVotemaker(voteMaker);
        vote.setMembers(Arrays.asList(voteMaker));
        return addVote(vote);
    }

    public Vote addVote(Vote vote) {
        vote.setId(0);
        for (Choice c : vote.getChoices())
            c.setVote(vote);
        VoteDAO.of(Controllers.getEntityManager()).persist(vote);
        return vote;
    }

//    @DELETE
//    @Path("{id}")
//    public int deleteVote(@PathParam("id") int id) {
//        Controllers.getEntityManager().getTransaction().begin();
//        Controllers.getEntityManager().remove(Controllers.executeParamRequest("Vote.findById", "id", id));
//        Controllers.getEntityManager().getTransaction().commit();
//        return 0;
//    }

    @POST
    @Path("{id}/ballots")
    public Ballot addBallot(@PathParam ("id") int id, Ballot ballot) {
        // TODO: check validity here
        BallotDAO.of(Controllers.getEntityManager()).persist(ballot);
        return ballot;
    }

//    @PATCH
//    @Path("{id}")
//    public int modifyDate(@PathParam ("id") int id, LocalDate date) {
//        Controllers.Votes.mapGet(id).setEndDate(date);
//        return 0;
//    }

    @GET
    @Path("private/invited")
    public List<Vote> getPrivateVotesForAUser() {
        //TODO: get the user id here
        int userid = 3;
        return VoteDAO.of(Controllers.getEntityManager()).findPrivateByUser(
                UserDAO.of(Controllers.getEntityManager()).findById(userid));
    }
}
