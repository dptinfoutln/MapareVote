package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.Collection;

@Path("publicvotes")
public class VoteResource {
    Controller<Vote> ctrl = new Controller<>();

    @GET
    public Collection<Vote> getVotes(@QueryParam("page_num") int pagenum,
                                     @QueryParam("page_size") int pagesize) {
        return ctrl.getList();
    }
}
