package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.BallotChoice;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BallotChoiceDAO extends DAO<BallotChoice> {

    public static BallotChoiceDAO of(EntityManager entityManager) {
        return new BallotChoiceDAO(entityManager);
    }

    private BallotChoiceDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<BallotChoice> findAll() {
        return entityManager.createNamedQuery("BallotChoice.findAll", BallotChoice.class).getResultList();
    }

    public List<BallotChoice> findByBallot(Ballot ballot) {
        return entityManager.createNamedQuery("BallotChoice.findByBallot", BallotChoice.class).setParameter("ballot", ballot).getResultList();
    }



}
