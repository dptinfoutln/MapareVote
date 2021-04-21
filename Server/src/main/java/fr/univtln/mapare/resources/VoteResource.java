package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
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
    public Vote addPublicVote(@Context SecurityContext securityContext, Vote vote) throws BusinessException {
        vote.setMembers(null);

        vote.setVotemaker(((User) securityContext.getUserPrincipal()));

        return addVote(vote);
    }

    @POST
    @JWTAuth
    @Path("private")
    public Vote addPrivateVote(@Context SecurityContext securityContext, Vote vote) throws BusinessException {
        User voteMaker = (User) securityContext.getUserPrincipal();
        vote.setVotemaker(voteMaker);
        vote.setMembers(Arrays.asList(voteMaker));
        return addVote(vote);
    }

    public Vote addVote(Vote vote) throws BusinessException {
        vote.setId(0);
        for (Choice c : vote.getChoices())
            c.setVote(vote);
        try {
            VoteDAO.of(Controllers.getEntityManager()).persist(vote);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
        return vote;
    }

    @DELETE
    @Path("{id}") // TODO: for testing purposes only, remove in prod
    public int deleteVote(@PathParam("id") int id) {
        VoteDAO.of(Controllers.getEntityManager()).remove(id);
        return 0;
    }

    @POST
    @JWTAuth
    @Path("{id}/ballots")
    public Ballot addBallot(@Context SecurityContext securityContext, @PathParam ("id") int id, Ballot ballot) throws BusinessException {
        // TODO: check validity here
        ballot.setVote(VoteDAO.of(Controllers.getEntityManager()).findById(id));
        try {
            BallotDAO.of(Controllers.getEntityManager()).persist(ballot);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
        return ballot;
    }

//    @PATCH
//    @Path("{id}")
//    public int modifyDate(@PathParam ("id") int id, LocalDate date) {
//        Controllers.Votes.mapGet(id).setEndDate(date);
//        return 0;
//    }

    @GET
    @JWTAuth
    @Path("private/invited")
    public List<Vote> getPrivateVotesForUser(@Context SecurityContext securityContext) {
        int userid = ((User) securityContext.getUserPrincipal()).getId();
        return VoteDAO.of(Controllers.getEntityManager()).findPrivateByUser(
                UserDAO.of(Controllers.getEntityManager()).findById(userid));
    }

    @GET
    @JWTAuth
    @Path("{id}/myballot")
    public Ballot getSpecificBallotforUser(@Context SecurityContext securityContext,
                                           @PathParam("id") int id) {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);
        User voter = (User) securityContext.getUserPrincipal();
        if (vote.getAnonymous().equals(false)) {
            return BallotDAO.of(Controllers.getEntityManager()).findByVoteByVoter(vote, voter);
        }
        else
            return null;
    }
}
