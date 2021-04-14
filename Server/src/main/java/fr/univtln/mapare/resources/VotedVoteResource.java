package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.VotedVote;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.Collection;

@Path("votedvotes")
public class VotedVoteResource {
    Controller<VotedVote> ctrl = new Controller<>();

    @GET
    public Collection<VotedVote> getVotedVotes(@QueryParam("page_num") int pagenum,
                                               @QueryParam("page_size") int pagesize) {
        return ctrl.getList();
    }
}
