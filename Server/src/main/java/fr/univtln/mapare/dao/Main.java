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

        // Find an user by his id
        List<User> personneList = entityManager.createNamedQuery("User.findById")
                .setParameter("id", 1)
                .getResultList();

        User personne1 = personneList.get(0);

        for (User p : personneList)
            System.out.println(p.getLastname());

        System.out.println(personneList.get(0).getId());

        // Find the voted votes concerning an user an user
        List<VotedVote> voteList1 = entityManager.createNamedQuery("VotedVotes.findByUser")
                .setParameter("user", personne1)
                .getResultList();

        // Find the voted votes concerning a vote
        List<VotedVote> voteList2 = entityManager.createNamedQuery("VotedVotes.findByVote")
                .setParameter("user", personneList.get(0))
                .getResultList();

        /*// Find public votes
        List<Vote> publicVoteList = entityManager.createNamedQuery("Vote.findPublic").getResultList();
        System.out.println(publicVoteList);*/


        entityManager.close();
    }
}
