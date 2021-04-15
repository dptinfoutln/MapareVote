package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

public class BallotDAO {
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");

    public static void persist(List<Choice> choices, User voter, Vote vote, LocalDateTime time) {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Persist Ballot
        Ballot B = new Ballot(time, vote);
        B.setVoter(voter); // TODO: change for anonymous
        entityManager.persist(B);
        entityManager.flush();

        for ( Choice C : choices) {
            // Persist Choice
            entityManager.persist(C);
            entityManager.flush();
            // Persist BallotChoice
            BallotChoice BC = new BallotChoice(B, C);
            entityManager.persist(BC);
            entityManager.flush();
            // Updates Ballot
            B.addChoice(BC);
            entityManager.persist(B);
            entityManager.flush();
        }
        transaction.commit();
        entityManager.close();
    }
}
