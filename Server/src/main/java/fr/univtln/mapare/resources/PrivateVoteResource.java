package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.PrivateVote;
import jakarta.ws.rs.*;

@Path("privatevotes")
public class PrivateVoteResource {
    static Controller<PrivateVote> ctrl = new Controller<>();
    static int lastId = 0; // init at highest ID + 1

    private static void foo() {
        lastId++;
    }

    @GET
    @Path("{id}")
    public PrivateVote getVote(@PathParam ("id") int id) {
        return ctrl.mapGet(id);
    }

    @POST
    public PrivateVote addVote(PrivateVote vote) {
        vote.setId(lastId);
        foo();
        ctrl.mapAdd(vote.getId(), vote);
        return vote;
    }
}
