package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.controllers.VoteUtils;
import fr.univtln.mapare.dao.*;
import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.exceptions.NotFoundException;
import fr.univtln.mapare.exceptions.TooEarlyException;
import fr.univtln.mapare.model.*;
import fr.univtln.mapare.security.annotations.JWTAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Path("votes")
public class VoteResource {

    @GET
    @Path("public")
    public List<Vote> getVotes(@QueryParam("page_num") int pagenum,
                               @QueryParam("page_size") int pagesize,
                               @QueryParam("name_like") String approxname,
                               @QueryParam("starts_with") String namestart,
                               @QueryParam("ends_with") String nameend,
                               @QueryParam("algo") String algoname,
                               @QueryParam("sort") String sortkey) throws ForbiddenException {
        if (pagenum == 0)
            pagenum = 1;
        if (pagesize == 0)
            pagesize = 20;
        if (algoname != null)
            algoname = algoname.replace("'", "%").replace("\"", "");

        return VoteDAO.of(Controllers.getEntityManager()).findAllPublic(pagenum, pagesize, approxname, namestart,
                nameend, algoname, sortkey);
    }

    @GET
    @JWTAuth
    @Path("{id}")
    public Vote getVote(@Context SecurityContext securityContext, @PathParam("id") int id) throws NotFoundException, ForbiddenException {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);

        if (vote == null)
            throw new NotFoundException();

//        Thread thread;
        if (vote.isPublic() || vote.getMembers().contains((User) securityContext.getUserPrincipal())) {
            if (vote.getEndDate() != null && (
                    (!vote.hasResults() || vote.isIntermediaryResult()) && LocalDate.now().isAfter(vote.getEndDate()))
            ) {
                vote.setIntermediaryResult(false);
//                thread = new Thread(VoteUtils.voteResultsOf(vote));
//                thread.start();
                VoteUtils.voteResultsOf(vote).calculateResults();
            }
            // We check if the voter count is big, if so we only update once a day max.
            if (vote.isIntermediaryResult() &&
                    (vote.getBallots().size() < 1000 ||
                            !vote.getLastCalculated().equals(LocalDate.now()))) {
                vote.setLastCalculated(LocalDate.now());
//                thread = new Thread(VoteUtils.voteResultsOf(vote));
//                thread.start();
                VoteUtils.voteResultsOf(vote).calculateResults();
            }
            return vote;
        }
        else
            throw new ForbiddenException("You don't have access to this vote's details.");
    }

    @GET
    @JWTAuth
    @Path("{id}/results")
    public List<VoteResult> getVoteResults(@Context SecurityContext securityContext,
                                           @PathParam("id") int id) throws NotFoundException, ForbiddenException, TooEarlyException {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);

        if (vote == null)
            throw new NotFoundException();

        if(vote.isPendingResult())
            throw new TooEarlyException();

        if (vote.isPublic() || vote.getMembers().contains((User) securityContext.getUserPrincipal())) {
            return vote.getResultList();
        }
        else
            throw new ForbiddenException();
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
        if (vote.getLabel() == null)
            throw new ForbiddenException("Please send a title.");
        if (vote.getAlgo() == null || !Vote.getAlgolist().contains(vote.getAlgo()))
            throw new ForbiddenException("Invalid algorithm");
        if (vote.getMaxChoices() < 1)
            throw new ForbiddenException("Please enter a proper value for your maxChoices count.");
        if (vote.getChoices().size() <= vote.getMaxChoices())
            throw new ForbiddenException("Please enter enough choices to reach your maxChoices count or lower your maxChoices count.");
        if (vote.getStartDate() == null)
            throw new ForbiddenException("Invalid start date.");
        if (vote.getStartDate().isBefore(LocalDate.now().minusDays(1)))
            throw new ForbiddenException("Start date before today.");
        if (vote.getEndDate() != null && vote.getEndDate().isBefore(vote.getStartDate().plus(1, ChronoUnit.DAYS)))
            throw new ForbiddenException("End date before start date.");
        if (vote.getEndDate() == null && !vote.isIntermediaryResult())
            throw new ForbiddenException("Vote with no end date and no intermediary results: invalid.");
        if (vote.getEndDate() == null && vote.getAlgo().equals("STV"))
            throw new ForbiddenException("Votes with the STV algorithm have to have an end date.");
        if (vote.getAlgo().equals("borda")){
            vote.setMaxChoices(vote.getChoices().size());
        }
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
        //TODO: check choices
        ballot.setId(0);
        User voter = (User) securityContext.getUserPrincipal();
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);
        if (vote == null)
            throw new NotFoundException("Vote does not exist.");
        if (voter.isBanned())
            throw new ForbiddenException("User is banned.");
        if (vote.isDeleted())
            throw new ForbiddenException("Vote deleted.");
        if (LocalDate.now().isBefore(vote.getStartDate()))
            throw new ForbiddenException("Too early.");
        if (vote.getEndDate() != null && LocalDate.now().isAfter(vote.getEndDate()))
            throw new ForbiddenException("Too late.");
        if (!vote.getAlgo().equals("STV") && ballot.getChoices().size() > vote.getMaxChoices())
            throw new ForbiddenException("Too many choices.");
//        for (BallotChoice bc : ballot.getChoices())
//            if (!vote.getChoices().contains(bc.getChoice()))
//                throw new ForbiddenException("Bad choice(s).");
        ballot.setDate(LocalDateTime.now());
        ballot.setVote(vote);
        switch (vote.getAlgo()) {
            case "majority":
                for (BallotChoice bc : ballot.getChoices()) {
                    bc.setWeight(1);
                }
                break;
            case "borda":
            case "STV":
                if (vote.getChoices().size() != ballot.getChoices().size())
                    throw new ForbiddenException("Not enough choices for " + vote.getAlgo() + " algorithm.");
                int[] temparray = new int[vote.getChoices().size()];
                for (BallotChoice bc : ballot.getChoices()) {
                    // We verify that all values are coherent.
                    if (bc.getWeight() > vote.getChoices().size() || bc.getWeight() <= 0)
                        throw new ForbiddenException("Invalid choice weight for " + vote.getAlgo() + " algorithm: "
                                + bc.getWeight() + ".");
                    if (temparray[bc.getWeight() - 1] != 0)
                        throw new ForbiddenException("Duplicate choice weight for " + vote.getAlgo() + " algorithm: "
                                + bc.getWeight() + ".");
                    temparray[bc.getWeight() - 1] = 1;
                }
                break;
            default:
                break;
        }
        ballot.setVoter(voter);
        ChoiceDAO choiceDAO = ChoiceDAO.of(Controllers.getEntityManager());
        Choice tempchoice;
        for (BallotChoice bc : ballot.getChoices()) {
            bc.setBallot(ballot);
            tempchoice = choiceDAO.findById(bc.getChoice().getId());
            if (tempchoice == null)
                throw new ForbiddenException("No such choice in database:" + bc.getChoice());
            if (!vote.getChoices().contains(tempchoice))
                throw new ForbiddenException("This choice is not for this vote:" + bc.getChoice());
            bc.setChoice(tempchoice);
        }
        try {
            BallotDAO.of(Controllers.getEntityManager()).persist(ballot);
            vote.addBallot(ballot);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }
        return ballot;
    }

    @GET
    @JWTAuth
    @Path("private/invited")
    public List<Vote> getPrivateVotesForUser(@Context SecurityContext securityContext) {
        int userid = ((User) securityContext.getUserPrincipal()).getId();
        return VoteDAO.of(Controllers.getEntityManager()).findPrivateByUser(
                UserDAO.of(Controllers.getEntityManager()).findById(userid));
    }

    @PATCH
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
