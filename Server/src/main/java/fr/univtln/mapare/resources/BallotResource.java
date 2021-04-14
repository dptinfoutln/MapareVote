package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDateTime;
import java.util.List;

@Path("ballots")
public class BallotResource {
    Controller<Ballot> ctrl = new Controller<>();

    @GET
    public List<Ballot> getBallots(@QueryParam("page_num") int pagenum,
                                 @QueryParam("page_size") int pagesize) {

        ctrl.listAdd(new Ballot(1, LocalDateTime.now(), new Vote()));
        return ctrl.getList();
    }
}
