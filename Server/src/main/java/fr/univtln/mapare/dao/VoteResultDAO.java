package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VoteResult;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * The type Vote result dao.
 */
public class VoteResultDAO extends GenericIdDAO<VoteResult> {

    /**
     * Of vote result dao.
     *
     * @param entityManager the entity manager
     * @return the vote result dao
     */
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

    /**
     * Find by vote list.
     *
     * @param vote the vote
     * @return the list
     */
    public List<VoteResult> findByVote(Vote vote) {
        return entityManager.createNamedQuery("VoteResult.findByVote", VoteResult.class).getResultList();
    }
}
