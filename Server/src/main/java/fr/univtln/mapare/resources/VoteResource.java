package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.controllers.MailUtils;
import fr.univtln.mapare.controllers.VoteUtils;
import fr.univtln.mapare.dao.*;
import fr.univtln.mapare.exceptions.*;
import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.exceptions.NotFoundException;
import fr.univtln.mapare.model.*;
import fr.univtln.mapare.security.annotations.JWTAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.StrictMath.ceil;

/**
 * The type Vote resource.
 */
@Path("votes")
public class VoteResource {
    /**
     * Gets votes.
     *
     * @param pagenum    the pagenum
     * @param pagesize   the pagesize
     * @param approxname the approxname
     * @param namestart  the namestart
     * @param nameend    the nameend
     * @param algoname   the algoname
     * @param sortkey    the sortkey
     * @param order      the order
     * @param open       the open
     * @return the votes
     */
    @GET
    @Path("public")
    public Response getVotes(@QueryParam("page_num") int pagenum,
                             @QueryParam("page_size") int pagesize,
                             @QueryParam("name_like") String approxname,
                             @QueryParam("starts_with") String namestart,
                             @QueryParam("ends_with") String nameend,
                             @QueryParam("algo") String algoname,
                             @QueryParam("sort") String sortkey,
                             @QueryParam("order") String order,
                             @QueryParam("open") boolean open) {
        return constructReponseAndPaginate(pagenum, pagesize, VoteDAO.of(Controllers.getEntityManager()).findAllPublic(
                new VoteQuery(approxname, namestart, nameend, algoname, sortkey, order, open)
        ));
    }

    /**
     * Gets vote.
     *
     * @param securityContext the security context
     * @param id              the id
     * @return the vote
     * @throws NotFoundException  the not found exception
     * @throws ForbiddenException the forbidden exception
     */
    @GET
    @JWTAuth
    @Path("{id}")
    public Vote getVote(@Context SecurityContext securityContext, @PathParam("id") int id) throws NotFoundException, ForbiddenException {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);

        if (vote == null)
            throw new NotFoundException();

        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

//        Thread thread;
        if (vote.isPublic() || vote.getMembers().contains(user)) {
            if (vote.getEndDate() != null && (
                    (!vote.hasResults() || vote.isIntermediaryResult()) && LocalDate.now().isAfter(vote.getEndDate().minusDays(1)))
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
        } else
            throw new ForbiddenException("You don't have access to this vote's details.");
    }

    /**
     * Gets vote results.
     *
     * @param securityContext the security context
     * @param id              the id
     * @return the vote results
     * @throws NotFoundException  the not found exception
     * @throws ForbiddenException the forbidden exception
     * @throws TooEarlyException  the too early exception
     */
    @GET
    @JWTAuth
    @Path("{id}/results")
    public List<VoteResult> getVoteResults(@Context SecurityContext securityContext,
                                           @PathParam("id") int id) throws NotFoundException, ForbiddenException, TooEarlyException {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);

        if (vote == null)
            throw new NotFoundException();

        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        if (vote.isPendingResult())
            throw new TooEarlyException();

        if (vote.isPublic() || vote.getMembers().contains(user)) {
            return vote.getResultList();
        } else
            throw new ForbiddenException();
    }

    /**
     * Add public vote vote.
     *
     * @param securityContext the security context
     * @param vote            the vote
     * @return the vote
     * @throws BusinessException the business exception
     */
    @POST
    @JWTAuth
    @Path("public")
    public Vote addPublicVote(@Context SecurityContext securityContext, Vote vote) throws BusinessException {
        vote.setMembers(null);

        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        vote.setVotemaker(user);

        return addVote(vote);
    }

    /**
     * Add private vote vote.
     *
     * @param securityContext the security context
     * @param vote            the vote
     * @return the vote
     * @throws BusinessException the business exception
     */
    @POST
    @JWTAuth
    @Path("private")
    public Vote addPrivateVote(@Context SecurityContext securityContext, Vote vote) throws BusinessException {
        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
        User voteMaker = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(voteMaker);

        vote.setVotemaker(voteMaker);
        List<User> memberList = new ArrayList<>(Collections.singletonList(voteMaker));

        User member;
        for (User m : vote.getMembers()) {
            member = dao.findByEmail(m.getEmail());
            // TODO: Exception if member is null
            if (member != null && !memberList.contains(member))
                memberList.add(member);
        }

        vote.setMembers(memberList);
        return addVote(vote);
    }

    /**
     * Add vote vote.
     *
     * @param vote the vote
     * @return the vote
     * @throws BusinessException the business exception
     */
    public Vote addVote(Vote vote) throws BusinessException {
        if (vote.getLabel() == null)
            throw new ForbiddenException("Please send a title.");
        if (vote.getAlgo() == null || !Vote.getAlgolist().contains(vote.getAlgo()))
            throw new ForbiddenException("Invalid algorithm");
        if (vote.getAlgo().equals("borda"))
            vote.setMaxChoices(vote.getChoices().size());
        if (vote.getMaxChoices() < 1)
            throw new ForbiddenException("Please enter a proper value for your maxChoices count.");
        if (vote.getChoices().size() < vote.getMaxChoices())
            throw new ForbiddenException("Please enter enough choices to reach your maxChoices count or lower your maxChoices count.");
        if (vote.getStartDate() == null)
            throw new ForbiddenException("Invalid start date.");
        if (vote.getStartDate().isBefore(LocalDate.now().minusDays(1)))
            throw new ForbiddenException("Start date before today.");
        if (vote.getEndDate() != null && vote.getEndDate().isBefore(vote.getStartDate().plusDays(1)))
            throw new ForbiddenException("End date before start date.");
        if (vote.getEndDate() == null && !vote.isIntermediaryResult())
            throw new ForbiddenException("Vote with no end date and no intermediary results: invalid.");
        if (vote.getEndDate() == null && vote.getAlgo().equals("STV"))
            throw new ForbiddenException("Votes with the STV algorithm have to have an end date.");
        vote.setId(0);
        for (Choice c : vote.getChoices())
            c.setVote(vote);
        try {
            VoteDAO.of(Controllers.getEntityManager()).persist(vote);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        }

        //TODO: limit number of thread if large amount of invited users
        for (User u : vote.getMembers())
            if (!u.equals(vote.getVotemaker()))
                new Thread(MailUtils.sendInvitationTo(vote, u)).start();
        return vote;
    }

    /**
     * Delete vote int.
     *
     * @param securityContext the security context
     * @param id              the id
     * @return the int
     * @throws NotFoundException  the not found exception
     * @throws ForbiddenException the forbidden exception
     */
    @DELETE
    @JWTAuth
    @Path("{id}")
    public int deleteVote(@Context SecurityContext securityContext,
                          @PathParam("id") int id) throws NotFoundException, ForbiddenException {
        VoteDAO dao = VoteDAO.of(Controllers.getEntityManager());
        Vote vote = dao.findById(id);

        if (vote == null)
            throw new NotFoundException("No such vote");

        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        if (!(user.isAdmin() || user.equals(vote.getVotemaker())))
            throw new ForbiddenException("You do not have the rights");

        vote.setDeleted(true);
        dao.update(vote);
        return 0;
    }

    /**
     * Add ballot ballot.
     *
     * @param securityContext the security context
     * @param id              the id
     * @param ballot          the ballot
     * @return the ballot
     * @throws BusinessException the business exception
     */
    @POST
    @JWTAuth
    @Path("{id}/ballots")
    public Ballot addBallot(@Context SecurityContext securityContext, @PathParam("id") int id, Ballot ballot) throws BusinessException {
        ballot.setId(0);
        User voter = (User) securityContext.getUserPrincipal();
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);
        if (vote == null)
            throw new NotFoundException("Vote does not exist.");
        Controllers.checkUser(voter);
        if (vote.isDeleted())
            throw new ForbiddenException("Vote deleted.");
        if (voter.getVotesOnWhichTheUserHasVoted().contains(vote))
            throw new ForbiddenException("Already voted.");
        if (LocalDate.now().isBefore(vote.getStartDate()))
            throw new ForbiddenException("Too early.");
        if (vote.getEndDate() != null && LocalDate.now().isAfter(vote.getEndDate().minusDays(1)))
            throw new ForbiddenException("Too late.");
        if (!vote.getAlgo().equals("STV") && ballot.getChoices().size() > vote.getMaxChoices())
            throw new ForbiddenException("Too many choices.");
        if (ballot.getChoices().isEmpty())
            throw new ForbiddenException("No choice(s).");
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

    /**
     * Gets private votes for user.
     *
     * @param securityContext the security context
     * @param pagenum         the page number
     * @param pagesize        the page size
     * @param approxname      the approximative name
     * @param namestart       the name's start
     * @param nameend         the name's end
     * @param algoname        the algorithm's name
     * @param sortkey         the sort key
     * @param order           the order parameter
     * @param open            the open boolean
     * @return the private votes for user
     * @throws ForbiddenException the forbidden exception
     */
    @GET
    @JWTAuth
    @Path("private/invited")
    public Response getPrivateVotesForUser(@Context SecurityContext securityContext,
                                           @QueryParam("page_num") int pagenum,
                                           @QueryParam("page_size") int pagesize,
                                           @QueryParam("name_like") String approxname,
                                           @QueryParam("starts_with") String namestart,
                                           @QueryParam("ends_with") String nameend,
                                           @QueryParam("algo") String algoname,
                                           @QueryParam("sort") String sortkey,
                                           @QueryParam("order") String order,
                                           @QueryParam("open") boolean open) throws ForbiddenException {
        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        return constructReponseAndPaginate(pagenum, pagesize, VoteDAO.of(Controllers.getEntityManager()).findPrivateByUser(
                UserDAO.of(Controllers.getEntityManager()).findById(user.getId()),
                new VoteQuery(approxname, namestart, nameend, algoname, sortkey, order, open)));
    }

    /**
     * Gets specific ballotfor user.
     *
     * @param securityContext the security context
     * @param id              the id
     * @return the specific ballotfor user
     * @throws ForbiddenException the forbidden exception
     */
    @GET
    @JWTAuth
    @Path("{id}/myballot")
    public Ballot getSpecificBallotforUser(@Context SecurityContext securityContext,
                                           @PathParam("id") int id) throws ForbiddenException {
        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);
        User voter = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(voter);

        if (!vote.isAnonymous()) {
            return BallotDAO.of(Controllers.getEntityManager()).findByVoteByVoter(vote, voter);
        } else
            return null;
    }

    /**
     * Gets started votes.
     *
     * @param securityContext the security context
     * @param pagenum         the pagenum
     * @param pagesize        the pagesize
     * @param approxname      the approxname
     * @param namestart       the namestart
     * @param nameend         the nameend
     * @param algoname        the algoname
     * @param sortkey         the sortkey
     * @param order           the order
     * @param open            the open
     * @return the started votes
     * @throws ForbiddenException the forbidden exception
     */
    @GET
    @JWTAuth
    @Path("startedvotes")
    public Response getStartedVotes(@Context SecurityContext securityContext, @QueryParam("page_num") int pagenum,
                                    @QueryParam("page_size") int pagesize,
                                    @QueryParam("name_like") String approxname,
                                    @QueryParam("starts_with") String namestart,
                                    @QueryParam("ends_with") String nameend,
                                    @QueryParam("algo") String algoname,
                                    @QueryParam("sort") String sortkey,
                                    @QueryParam("order") String order,
                                    @QueryParam("open") boolean open) throws ForbiddenException {
        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        return constructReponseAndPaginate(pagenum, pagesize, VoteDAO.of(Controllers.getEntityManager())
                .findByVotemaker(user,
                        new VoteQuery(approxname, namestart, nameend, algoname, sortkey, order, open)
                )
        );
    }

    /**
     * Gets voted votes.
     *
     * @param securityContext the security context
     * @param pagenum         the pagenum
     * @param pagesize        the pagesize
     * @param approxname      the approxname
     * @param namestart       the namestart
     * @param nameend         the nameend
     * @param algoname        the algoname
     * @param sortkey         the sortkey
     * @param order           the order
     * @param open            the open
     * @return the voted votes
     * @throws ForbiddenException the forbidden exception
     */
    @GET
    @JWTAuth
    @Path("votedvotes")
    public Response getVotedVotes(@Context SecurityContext securityContext, @QueryParam("page_num") int pagenum,
                                  @QueryParam("page_size") int pagesize,
                                  @QueryParam("name_like") String approxname,
                                  @QueryParam("starts_with") String namestart,
                                  @QueryParam("ends_with") String nameend,
                                  @QueryParam("algo") String algoname,
                                  @QueryParam("sort") String sortkey,
                                  @QueryParam("order") String order,
                                  @QueryParam("open") boolean open) throws ForbiddenException {
        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        return constructReponseAndPaginate(pagenum, pagesize, VoteDAO.of(Controllers.getEntityManager())
                .findByVoter(user,
                        new VoteQuery(approxname, namestart, nameend, algoname, sortkey, order, open)
                )
        );
    }

    private Response constructReponseAndPaginate(int pagenum, int pagesize, List<Vote> voteList) {
        if (pagenum <= 0)
            pagenum = 1;
        if (pagesize <= 0)
            pagesize = 20;

        int size = voteList.size();

        voteList = voteList.stream().skip((pagenum - 1) * (long) pagesize).limit(pagesize).collect(Collectors.toList());

        Response.ResponseBuilder rb = Response.ok(voteList);
        return rb.header("votecount", "" + size)
                .header("pagecount", "" + ceil((double) size / pagesize))
                .build();
    }

    /**
     * Gets token list for vote.
     *
     * @param securityContext the security context
     * @param id              the id
     * @return the token list for vote
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    @GET
    @JWTAuth
    @Path("{id}/tokens")
    public Map<String, String> getTokenListForVote(@Context SecurityContext securityContext,
                                           @PathParam("id") int id) throws ForbiddenException, NotFoundException {
        User user = (User) securityContext.getUserPrincipal();

        Controllers.checkUser(user);

        Vote vote = VoteDAO.of(Controllers.getEntityManager()).findById(id);

        if (vote == null)
            throw new NotFoundException("No vote found with id: " + id);

        if (vote.isPrivate() && !vote.getMembers().contains(user))
            throw new ForbiddenException("You do not have the access rights for this vote.");

        List<VotedVote> votedVotes = VotedVoteDAO.of(Controllers.getEntityManager()).findByVote(vote);

        Map<String, String> returnMap = new HashMap<>();

        if (votedVotes == null)
            return returnMap;

        for (VotedVote vv : votedVotes) {
            if (vote.isAnonymous())
                returnMap.put(vv.getToken(), "");
            else
                returnMap.put(vv.getToken(), vv.getUser().getLastname() + " " + vv.getUser().getFirstname());
        }

        return returnMap;
    }
}
