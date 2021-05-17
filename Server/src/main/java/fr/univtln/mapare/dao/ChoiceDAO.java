package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ChoiceDAO extends GenericIdDAO<Choice> {

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

    public List<Choice> findAll(int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Choice.findAll", Choice.class)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();
    }

    public List<Choice> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Choice.findByVote", Choice.class).setParameter("vote", vote).getResultList();
    }

    public List<Choice> findByVote(Vote vote, int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Choice.findByVote", Choice.class)
                .setParameter("vote", vote)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();
    }


}
