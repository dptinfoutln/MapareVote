package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.BallotChoice;
import jakarta.ws.rs.*;

import java.util.Collection;

//@Path("ballots")
//public class BallotResource {
//
//    @GET
//    @Path("{id}")
//    public Ballot getBallot(@PathParam("id") int id) {
//        return ctrl.mapGet(id);
//    }
//
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
//}
