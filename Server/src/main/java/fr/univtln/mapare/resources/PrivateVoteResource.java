package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.PrivateVote;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDate;
import java.util.List;

@Path("privatevotes")
public class PrivateVoteResource {
    Controller<PrivateVote> ctrl = new Controller<>();


    @GET
    @Path("{id}")
    public List<PrivateVote> getVotes(@PathParam ("id") int id,
                                      @QueryParam("page_num") int pagenum,
                                      @QueryParam("page_size") int pagesize) {
        ctrl.listAdd(new PrivateVote(new Vote(1, "Testvote", LocalDate.now(), LocalDate.now(), "majority", false, new User(1, "test@example.com", "Dupont", "Thomas", "TESTX5", true, true, false))));
        return ctrl.getList();
    }
}
