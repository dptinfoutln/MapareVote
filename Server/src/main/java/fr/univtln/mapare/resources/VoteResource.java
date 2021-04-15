package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.*;

import java.util.Collection;

@Path("votes")
public class VoteResource {
    Controller<Vote> ctrl = new Controller<>();
    static int lastId = 0; // init at highest ID + 1

    @GET
    @Path("public")
    public Collection<Vote> getVotes(@QueryParam("page_num") int pagenum,
                                     @QueryParam("page_size") int pagesize) {
        return ctrl.getList();
    }

    private static void foo() {
        lastId++;
    }

    @GET
    @Path("{id}")
    public Vote getVote(@PathParam("id") int id) {
        return ctrl.mapGet(id);
    }

    @POST
    public Vote addVote(Vote vote) {
        vote.setId(lastId);
        foo();
        ctrl.mapAdd(vote.getId(), vote);
        return vote;
    }
}
