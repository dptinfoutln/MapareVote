package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.exceptions.NotFoundException;
import fr.univtln.mapare.model.*;
import fr.univtln.mapare.security.annotations.JWTAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public Vote getVote(@Context SecurityContext securityContext, @PathParam("id") int id) throws NotFoundException {
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
        if (vote.getMaxChoices() < 1)
            throw new ForbiddenException("Please enter a proper value for your maxChoices count.");
        if (vote.getChoices().size() < vote.getMaxChoices())
            throw new ForbiddenException("Please enter enough choices to reach your maxChoices count or lower your maxChoices count.");
        if (vote.getChoices().size() == 1)
            throw new ForbiddenException("Please offer more than one choice in this vote.");
        if (vote.getStartDate().isBefore(LocalDate.now()))
            throw new ForbiddenException("Start date before today.");
        if (vote.getEndDate() != null && vote.getEndDate().isBefore(vote.getStartDate().plus(1, ChronoUnit.DAYS)))
            throw new ForbiddenException("End date before start date.");
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
        User voter = (User) securityContext.getUserPrincipal();
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);
        if (voter.isBanned())
            throw new ForbiddenException("User is banned.");
        if (vote.isDeleted())
            throw new ForbiddenException("Vote deleted.");
        if (ballot.getDate().isAfter(ChronoLocalDateTime.from(vote.getEndDate())))
            throw new ForbiddenException("Too late.");
        for (BallotChoice bc : ballot.getChoices())
            if (bc.getChoice().getVote() != vote)
                throw new ForbiddenException("Bad choice(s).");
        ballot.setVote(vote);
        ballot.setVote(vote);
        ballot.setVoter(voter);
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
        if (!vote.isAnonymous()) {
            return BallotDAO.of(Controllers.getEntityManager()).findByVoteByVoter(vote, voter);
        }
        else
            return null;
    }
}
