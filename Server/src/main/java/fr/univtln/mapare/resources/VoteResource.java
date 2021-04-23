package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.dao.VoteResultDAO;
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
        if (pagenum == 0)
            pagenum = 1;
        if (pagesize == 0)
            pagesize = 20;
        return VoteDAO.of(Controllers.getEntityManager()).findAllPublic(pagenum, pagesize);
    }

    @GET
    @JWTAuth
    @Path("{id}")
    public Vote getVote(@Context SecurityContext securityContext, @PathParam("id") int id) throws NotFoundException, ForbiddenException {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);

        if (vote == null)
            throw new NotFoundException();

        if (vote.isPublic() || vote.getMembers().contains((User) securityContext.getUserPrincipal())) {
            if (vote.getEndDate() != null && (
                    (!vote.hasResults() || vote.isIntermediaryResult()) && LocalDate.now().isAfter(vote.getEndDate()))
            ) {
                vote.setIntermediaryResult(false);
                vote.calculateResults();
                if (vote.getResultList() == null)
                    throw new NotFoundException("Algorithm not implemented.");
                VoteDAO.of(Controllers.getEntityManager()).update(vote);
            }
            if (vote.isIntermediaryResult()) {
                vote.calculateResults();
                if (vote.getResultList() == null)
                    throw new NotFoundException("Algorithm not implemented.");
                VoteDAO.of(Controllers.getEntityManager()).update(vote);
            }
            return vote;
        }
        else
            throw new ForbiddenException("You don't have access to this vote's details.");
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
        if (!Vote.getAlgolist().contains(vote.getAlgo()))
            throw new ForbiddenException("Invalid algorithm");
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
        if (vote.getEndDate() == null && !vote.isIntermediaryResult())
            throw new ForbiddenException("Vote with no end date and no intermediary results: invalid.");
        vote.setId(0);
        if (vote.getAlgo() == "borda"){
            vote.setMaxChoices(vote.getChoices().size());
        }
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
        //TODO: check borda weight 0.
        //TODO: check maxchoices
        User voter = (User) securityContext.getUserPrincipal();
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);
        if (voter.isBanned())
            throw new ForbiddenException("User is banned.");
        if (vote.isDeleted())
            throw new ForbiddenException("Vote deleted.");
        if (vote.getEndDate() != null && ballot.getDate().isAfter(ChronoLocalDateTime.from(vote.getEndDate())))
            throw new ForbiddenException("Too late.");
        if (ballot.getChoices().size() > vote.getMaxChoices())
            throw new ForbiddenException("Too many choices.");
//        for (BallotChoice bc : ballot.getChoices())
//            if (!vote.getChoices().contains(bc.getChoice()))
//                throw new ForbiddenException("Bad choice(s).");
        ballot.setVote(vote);
        switch (vote.getAlgo()) {
            case "majority":
                for (BallotChoice bc : ballot.getChoices()) {
                    bc.setWeight(1);
                }
                break;
            case "borda":
                int[] temparray = new int[vote.getChoices().size()];
                for (BallotChoice bc : ballot.getChoices()) {
                    // We verify that all values are coherent for borda count.
                    if (bc.getWeight() > vote.getChoices().size())
                        throw new ForbiddenException("Invalid choice weight for borda count algorithm.");
                    if (temparray[bc.getWeight() - 1] != 0)
                        throw new ForbiddenException("Duplicate choice weight for borda count algorithm.");
                    temparray[bc.getWeight() - 1] = 1;
                }
                break;
            case "STV":
            default:
                break;
        }
        ballot.setVoter(voter);
        for (BallotChoice bc : ballot.getChoices()) {
            bc.setBallot(ballot);
        }
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
