package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VotedVote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");

    public static void main(String[] args) {

        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        /*User personne = new User("test@test.com",
                "TEST",
                "test",
                "AZERTY",
                true,
                false,
                false,
                false);

        entityManager.persist(personne);

        entityManager.flush();

        transaction.commit();*/

        /*List<User> userList = entityManager.createNamedQuery("findUserWithId")
                .setParameter("id", 1)
                .getResultList();

        for (User u : userList) {
            System.out.println(u.getLastname());
        }*/

        /*List<Vote> voteList = entityManager.createNamedQuery("findVotedVotesWithUserId")
                .setParameter("id", 1)
                .getResultList();

        for (Vote v : voteList) {
            System.out.println(v.getId());
        }*/

        entityManager.close();
    }
}
