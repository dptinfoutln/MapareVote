package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.BallotDAO;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.*;

import java.time.LocalDate;
import java.util.*;

@Path("votes")
public class VoteResource {
//    static Controller<Vote> ctrl = new Controller<>();

//    public VoteResource() {
//        if (lastId == -1) {
//            Controllers.loadVotes();
//            Controllers.loadUsers();
//            int maxi = Controllers.Votes.getList().stream().max(Comparator.comparingInt(Vote::getId)).get().getId();
//            lastId = maxi + 1;
//        }
//    }

    @GET
    @Path("public")
    @SuppressWarnings("unchecked")
    public List<Vote> getVotes(@QueryParam("page_num") int pagenum,
                         @QueryParam("page_size") int pagesize) {
        return (List<Vote>) Controllers.executeRequest("Vote.findPublic");
    }

    @GET
    @Path("public/{id}")
    public Vote getVote(@PathParam("id") int id) {
        return (Vote) Controllers.executeParamRequest("Vote.findById", "id", id).get(0);
    }

    @POST
    @Path("public")
    public Vote addVote(Vote vote) {
        vote.setId(0);
        for (Choice c : vote.getChoices())
            c.setVote(vote);
        Controllers.getEntityManager().getTransaction().begin();
        Controllers.getEntityManager().persist(vote);
        Controllers.getEntityManager().flush();
        Controllers.getEntityManager().getTransaction().commit();
        return vote;
    }

//    @DELETE
//    @Path("{id}")
//    public int deleteVote(@PathParam("id") int id) {
//        Controllers.getEntityManager().getTransaction().begin();
//        Controllers.getEntityManager().remove(Controllers.executeParamRequest("Vote.findById", "id", id));
//        Controllers.getEntityManager().getTransaction().commit();
//        return 0;
//    }

//    @POST
//    @Path("{id}/choice")
//    public Choice addChoice(@PathParam("id") int id, Choice choice) {
//        Vote vote = Controllers.PrivateVotes.mapGet(id);
//        choice.setVote(vote);
//        vote.addChoice(choice);
//        return choice;
//    }

    @POST
    @Path("{id}/ballots")
    public Ballot addBallot(@PathParam ("id") int id, Ballot ballot) {
        // TODO: check validity here
        BallotDAO.persist(ballot, id, 1);
        return ballot;
    }

//    @PATCH
//    @Path("{id}")
//    public int modifyDate(@PathParam ("id") int id, LocalDate date) {
//        Controllers.Votes.mapGet(id).setEndDate(date);
//        return 0;
//    }

    public static void main(String[] args) {
        Controllers.init();
        Vote qsd = new Vote("Testpersist",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                "pasimplemente",
                false,
                (User) Controllers.executeRequest("User.findAll").get(0));
        qsd.addChoice(new Choice(Collections.singletonList("choix1"), qsd));
        qsd.addChoice(new Choice(Collections.singletonList("choix2"), qsd));
        qsd.addChoice(new Choice(Collections.singletonList("choix3"), qsd));
        Controllers.getEntityManager().getTransaction().begin();
        Controllers.getEntityManager().persist(qsd);
        Controllers.getEntityManager().getTransaction().commit();
        Controllers.close();
    }
}
