package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * The type Choice dao.
 */
public class ChoiceDAO extends GenericIdDAO<Choice> {

    /**
     * Of choice dao.
     *
     * @param entityManager the entity manager
     * @return the choice dao
     */
    public static ChoiceDAO of(EntityManager entityManager) {
        return new ChoiceDAO(entityManager);
    }

    private ChoiceDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Choice> findAll() {
        return entityManager.createNamedQuery("Choice.findAll", Choice.class).getResultList();
    }

    /**
     * Find all list.
     *
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the list
     */
    public List<Choice> findAll(int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Choice.findAll", Choice.class)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();
    }

    /**
     * Find by vote list.
     *
     * @param vote the vote
     * @return the list
     */
    public List<Choice> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Choice.findByVote", Choice.class).setParameter("vote", vote).getResultList();
    }

    /**
     * Find by vote list.
     *
     * @param vote      the vote
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the list
     */
    public List<Choice> findByVote(Vote vote, int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Choice.findByVote", Choice.class)
                .setParameter("vote", vote)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();
    }


}
