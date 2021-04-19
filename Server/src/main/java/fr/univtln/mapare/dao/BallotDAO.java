package fr.univtln.mapare.dao;


import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BallotDAO extends DAO<Ballot> {

    public static BallotDAO of(EntityManager entityManager) {
        return new BallotDAO(entityManager);
    }

    private BallotDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    List<Ballot> findAll() {
        return entityManager.createNamedQuery("Ballot.findAll", Ballot.class).getResultList();
    }

    public List<Ballot> findByVoter(User voter) {
        return entityManager.createNamedQuery("Ballot.findByVoter", Ballot.class).setParameter("voter", voter).getResultList();
    }

    public List<Ballot> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Ballot.findByVote", Ballot.class).setParameter("vote", vote).getResultList();
    }

//    public static void persist(Ballot ballot, int voteId, int userId) {
////        Vote vote = Controllers.Votes.mapGet(voteId);
//        EntityManager entityManager = Controllers.getEntityManager();
//        EntityTransaction trans = entityManager.getTransaction();
//        trans.begin();
//        Vote vote = (Vote) Controllers.executeParamRequest("Vote.findById", "id", voteId).get(0);
//        ballot.setVote(vote);
////        ballot.setVoter(Controllers.Users.mapGet(userId));
//        ballot.setVoter((User) Controllers.executeParamRequest("User.findById", "id", userId).get(0));
//        List<BallotChoice> templist = ballot.getChoices();
//        ballot.setChoices(null);
//        entityManager.persist(ballot);
//        entityManager.flush();
//        for (BallotChoice bc : templist) {
//            bc.setBallot(ballot);
//            bc.getChoice().setVote(vote);
//            entityManager.persist(bc);
//        }
//        entityManager.flush();
//        trans.commit();
//        ballot.setChoices(templist);
//
//    }

}
