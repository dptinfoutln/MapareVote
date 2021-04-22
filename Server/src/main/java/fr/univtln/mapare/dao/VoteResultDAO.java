package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VoteResult;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VoteResultDAO extends GenericIdDAO<VoteResult> {

    public static VoteResultDAO of(EntityManager entityManager) {
        return new VoteResultDAO(entityManager);
    }

    private VoteResultDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<VoteResult> findAll() {
        return entityManager.createNamedQuery("VoteResult.findAll", VoteResult.class).getResultList();
    }

    public List<VoteResult> findByVote(Vote vote) {
        return entityManager.createNamedQuery("VoteResult.findByVote", VoteResult.class).getResultList();
    }
}
