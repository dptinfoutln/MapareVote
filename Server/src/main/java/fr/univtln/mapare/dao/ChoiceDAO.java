package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ChoiceDAO extends DAO<Choice> {

    public static ChoiceDAO of(EntityManager entityManager) {
        return new ChoiceDAO(entityManager);
    }

    private ChoiceDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    List<Choice> findAll() {
        return entityManager.createNamedQuery("Choice.findAll", Choice.class).getResultList();
    }

    public List<Choice> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Choice.findByVote", Choice.class).getResultList();
    }


}
