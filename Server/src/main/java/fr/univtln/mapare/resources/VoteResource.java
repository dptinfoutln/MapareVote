package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.security.MySecurityContext;
import fr.univtln.mapare.security.annotations.JWTAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.util.*;

@Path("votes")
public class VoteResource {

    @GET
    @Path("public")
    public List<Vote> getVotes(@QueryParam("page_num") int pagenum,
                         @QueryParam("page_size") int pagesize) {
        return VoteDAO.of(Controllers.getEntityManager()).findAllPublic();
    }

    @GET
    @JWTAuth
    @Path("{id}")
    public Vote getVote(@Context SecurityContext securityContext, @PathParam("id") int id) {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);
        if (vote == null)
            throw new NotFoundException();
        if (vote.isPublic() || vote.getMembers().contains((User) securityContext.getUserPrincipal()))
            return vote;
        else
            return null;
    }

    @POST
    @JWTAuth
    @Path("public")
    public Vote addPublicVote(@Context SecurityContext securityContext, Vote vote) {
        vote.setMembers(null);

        vote.setVotemaker(((User) securityContext.getUserPrincipal()));

        return addVote(vote);
    }

    @POST
    @JWTAuth
    @Path("private")
    public Vote addPrivateVote(@Context SecurityContext securityContext, Vote vote) {
        User voteMaker = (User) securityContext.getUserPrincipal();
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

    @DELETE
    @Path("{id}") // TODO: for testing purposes only, remove in prod
    public int deleteVote(@PathParam("id") int id) {
        VoteDAO.of(Controllers.getEntityManager()).remove(id);
        return 0;
    }

    @POST
    @Path("{id}/ballots")
    public Ballot addBallot(@Context SecurityContext securityContext, @PathParam ("id") int id, Ballot ballot) {
        // TODO: check validity here
        ballot.setVote(VoteDAO.of(Controllers.getEntityManager()).findById(id));
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
