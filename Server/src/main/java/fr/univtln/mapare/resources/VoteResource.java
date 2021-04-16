package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.BallotChoice;
import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.ws.rs.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Path("votes")
public class VoteResource {
//    static Controller<Vote> ctrl = new Controller<>();
    static int lastId = -1; // init at highest ID + 1

    public VoteResource() {
        if (lastId == -1) {
            Controllers.loadPublicVotes();
            Controllers.loadUsers();
            int maxi = Controllers.PublicVotes.getList().stream().max(Comparator.comparingInt(Vote::getId)).get().getId();
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
        return Controllers.PublicVotes.getList();
    }

    @GET
    @Path("public/{id}")
    public Vote getVote(@PathParam("id") int id) {
        return Controllers.PublicVotes.mapGet(id);
    }

    @POST
    @Path("public") // Maybe remove later idk
    public Vote addVote(Vote vote) {
        //TODO: make it work
        vote.setId(lastId);
        foo();
        Controllers.PublicVotes.mapAdd(vote.getId(), vote);
        return vote;
    }

    @GET
    @Path("private/{id}")
    public Vote getPrivateVote(@PathParam ("id") int id) {
        return Controllers.PrivateVotes.mapGet(id);
    }

    @POST
    @Path("private")
    public Vote addPrivateVote(Vote vote) {
        //TODO: make it work
        vote.setId(lastId);
        foo();
        Controllers.PrivateVotes.mapAdd(vote.getId(), vote);
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
        Controllers.PublicVotes.mapGet(id).setEndDate(date);
        return 0;
    }
}
