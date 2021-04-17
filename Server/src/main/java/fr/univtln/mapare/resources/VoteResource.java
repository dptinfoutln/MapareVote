package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.*;

import java.time.LocalDate;
import java.util.*;

@Path("votes")
public class VoteResource {
//    static Controller<Vote> ctrl = new Controller<>();
    static int lastId = -1; // init at highest ID + 1

    public VoteResource() {
        if (lastId == -1) {
            Controllers.loadVotes();
            Controllers.loadUsers();
            int maxi = Controllers.Votes.getList().stream().max(Comparator.comparingInt(Vote::getId)).get().getId();
            lastId = maxi + 1;
        }
    }


    private static void foo() {
        lastId++;
    }

    @GET
    @Path("public")
    public Collection<Vote> getVotes(@QueryParam("page_num") int pagenum,
                                     @QueryParam("page_size") int pagesize) {
        return Controllers.Votes.getList();
    }

    @GET
    @Path("public/{id}")
    public Vote getVote(@PathParam("id") int id) {
        return Controllers.Votes.mapGet(id);
    }

    @POST
    @Path("public") // Maybe remove later idk
    public Vote addVote(Vote vote) {
        //TODO: make it work
        vote.setId(lastId);
        foo();
        Controllers.Votes.mapAdd(vote.getId(), vote);
        return vote;
    }

//    @POST
//    @Path("{id}/choice")
//    public Choice addChoice(@PathParam("id") int id, Choice choice) {
//        Vote vote = Controllers.PrivateVotes.mapGet(id);
//        choice.setVote(vote);
//        vote.addChoice(choice);
//        return choice;
//    }

    @POST
    @Path("{id}/ballots")
    public Ballot addBallot(@PathParam ("id") int id, Ballot ballot) {
        // TODO: check validity here
        BallotDAO.persist(ballot, id, 1);
        return ballot;
    }

    @PATCH
    @Path("{id}")
    public int modifyDate(@PathParam ("id") int id, LocalDate date) {
        Controllers.Votes.mapGet(id).setEndDate(date);
        return 0;
    }
}
