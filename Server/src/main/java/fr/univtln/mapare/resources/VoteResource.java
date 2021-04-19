package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
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
    @SuppressWarnings("unchecked")
    public List<Vote> getVotes(@QueryParam("page_num") int pagenum,
                         @QueryParam("page_size") int pagesize) {
        return (List<Vote>) Controllers.executeRequest("Vote.findPublic");
    }

    @GET
    @Path("{id}")
    public Vote getVote(@PathParam("id") int id) {
        //TODO: Check if the user can see this vote (memberlist, etc.)
        return (Vote) Controllers.executeParamRequest("Vote.findById", "id", id).get(0);
    }

    @POST
    @Path("public")
    public Vote addPublicVote(Vote vote) {
        vote.setVotemaker((User) Controllers.executeParamRequest("User.findById", "id", vote.getVotemaker().getId()).get(0));
        vote.setMembers(null);
        return addVote(vote);
    }

    @POST
    @Path("private")
    public Vote addPrivateVote(Vote vote) {
        User voteMaker = (User) Controllers.executeParamRequest("User.findById", "id", vote.getVotemaker().getId()).get(0);
        vote.setVotemaker(voteMaker);
        vote.setMembers(Arrays.asList(voteMaker));
        return addVote(vote);
    }

    public Vote addVote(Vote vote) {
        vote.setId(0);
        for (Choice c : vote.getChoices())
            c.setVote(vote);
        Controllers.getEntityManager().getTransaction().begin();
        Controllers.getEntityManager().persist(vote);
        Controllers.getEntityManager().flush();
        Controllers.getEntityManager().getTransaction().commit();
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
        BallotDAO.persist(ballot, id, 1);
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
        return Controllers.getEntityManager().createNamedQuery("Vote.findPrivateByUser").setParameter("user",
                Controllers.executeParamRequest("User.findById", "id",3).get(0)).getResultList();
    }
}
