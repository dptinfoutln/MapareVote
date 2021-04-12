package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDate;
import java.util.List;

@Path("publicvotes")
public class VoteResource {
    Controller<Vote> ctrl = new Controller<>();

    @GET
    public List<Vote> getVotes(@QueryParam("page_num") int pagenum,
                               @QueryParam("page_size") int pagesize) {
        ctrl.listAdd(new Vote(1, "Testvote", LocalDate.now(), LocalDate.now(), "majority", false, new User(1, "test@example.com", "Dupont", "Thomas", "TESTX5", true, true, false)));
        return ctrl.getList();
    }
}
