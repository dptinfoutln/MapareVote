package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.PrivateVote;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Path("votes")
public class VoteResource {
    static Controller<Vote> ctrl = new Controller<>();
    static int lastId = 0; // init at highest ID + 1
    static Map<Integer, Vote> publicVotes = new HashMap<>();
    static Map<Integer, PrivateVote> privateVotes = new HashMap<>();

    private static void foo() {
        lastId++;
    }

    @GET
    @Path("public")
    public Collection<Vote> getVotes(@QueryParam("page_num") int pagenum,
                                     @QueryParam("page_size") int pagesize) {
        return publicVotes.values();
    }

    @GET
    @Path("public/{id}")
    public Vote getVote(@PathParam("id") int id) {
        return publicVotes.get(id);
    }

    @POST
    @Path("public") // Maybe remove later idk
    public Vote addVote(Vote vote) {
        vote.setId(lastId);
        foo();
        ctrl.mapAdd(vote.getId(), vote);
        publicVotes.put(vote.getId(), vote);
        return vote;
    }

    @GET
    @Path("private/{id}")
    public PrivateVote getPrivateVote(@PathParam ("id") int id) {
        return privateVotes.get(id);
    }

    @POST
    @Path("private")
    public PrivateVote addVote(PrivateVote vote) {
        vote.setId(lastId);
        foo();
        ctrl.mapAdd(vote.getId(), vote);
        privateVotes.put(vote.getId(), vote);
        return vote;
    }

    @POST
    @Path("{id}/choice")
    public Choice addChoice(@PathParam("id") int id, Choice choice) {
        Vote vote = ctrl.mapGet(id);
        choice.setVote(vote);
        vote.addChoice(choice);
        return choice;
    }
}
