package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.BallotChoice;
import jakarta.ws.rs.*;

import java.util.Collection;

@Path("ballots")
public class BallotResource {
    static Controller<Ballot> ctrl = new Controller<>();
    static int lastId = 0;

//    @GET
//    public Collection<Ballot> getBallots(@QueryParam("page_num") int pagenum,
//                                         @QueryParam("page_size") int pagesize) {
//        return ctrl.getList();
//    }

    private static void foo() {
        lastId++;
    }

    @GET
    @Path("{id}")
    public Ballot getBallot(@PathParam("id") int id) {
        return ctrl.mapGet(id);
    }

//    @POST
//    public Ballot addBallot(Ballot ballot) {
//        ballot.setId(lastId);
//        foo();
//        ctrl.mapAdd(ballot.getId(), ballot);
//        return ballot;
//    }
//
//    @POST
//    @Path("{id}/ballotchoice")
//    public int addChoice(@PathParam("id") int id, BallotChoice choice) {
//        ctrl.mapGet(id).addChoice(choice);
//        return 0;
//    }
}
