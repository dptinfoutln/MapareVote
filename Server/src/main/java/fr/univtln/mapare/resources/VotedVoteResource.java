package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.VotedVote;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("votedvotes")
public class VotedVoteResource {
    Controller<VotedVote> ctrl = new Controller<>();

    @GET
    public List<VotedVote> getVotedVotes(@QueryParam("page_num") int pagenum,
                                         @QueryParam("page_size") int pagesize) {

        ctrl.listAdd(new VotedVote("token", new Vote(), new User()));
        return ctrl.getList();
    }
}
