package fr.univtln.mapare;

import static org.junit.Assert.assertTrue;

import fr.univtln.mapare.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MainTest {
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");

    User personneTest = new User("nico@nico.mail",
            "MARGUERIT",
            "Nicolas",
            "AZERTY");

    Vote voteTest = new Vote("Est-ce que les tests sont toujours utiles ?",
            LocalDate.now(),
            null,
            "algoDeTest",
            false,
            false,
            personneTest);

    Choice choiceTest = new Choice(Arrays.asList("oui_test", "non_test", "blanc_test"),
            voteTest);

    private User getUserTest()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        User userTest = (User) entityManager.createNamedQuery("User.findByEmail").setParameter("email", personneTest.getEmail()).getResultList().get(0);

        entityManager.flush();
        transaction.commit();
        entityManager.close();

        return userTest;
    }

    private Vote getVoteTest()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Vote voteTest = (Vote) entityManager.createNamedQuery("Vote.findByVotemaker").setParameter("votemaker", getUserTest()).getResultList().get(0);

        entityManager.flush();
        transaction.commit();
        entityManager.close();

        return voteTest;
    }

    private Choice getChoiceTest()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Choice choiceTest = (Choice) entityManager.createNamedQuery("Choice.findByVote").setParameter("vote", getVoteTest()).getResultList().get(0);

        entityManager.flush();
        transaction.commit();
        entityManager.close();

        return choiceTest;
    }

    @Test
    public void persistUser()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(personneTest);

        entityManager.flush();
        transaction.commit();
        entityManager.close();
    }

    @Test
    public void persistVote()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        User personne = getUserTest();
        voteTest.setVotemaker(personne);

        entityManager.persist(voteTest);

        entityManager.flush();
        transaction.commit();
        entityManager.close();
    }

    @Test
    public void persistChoice()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Vote vote = getVoteTest();
        choiceTest.setVote(vote);

        entityManager.persist(choiceTest);

        entityManager.flush();
        transaction.commit();
        entityManager.close();
    }

    /*@Test
    public void persistBallot() {
        Vote votetest = getVoteTest();
        Ballot ballotTest = new Ballot(LocalDateTime.now(), voteTest);
        Choice choiceTest1 = new Choice(Arrays.asList("oui_test"),
                voteTest);
        Choice choiceTest2 = new Choice(Arrays.asList("non_test"),
                voteTest);
        Choice choiceTest3 = new Choice(Arrays.asList("blanc_test"),
                voteTest);

        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(ballotTest);
        entityManager.flush();
        entityManager.persist(choiceTest1);
        entityManager.flush();
        entityManager.persist(choiceTest2);
        entityManager.flush();
        entityManager.persist(choiceTest3);
        entityManager.flush();

        ballotTest.addChoice(new BallotChoice(ballotTest, choiceTest1));
        ballotTest.addChoice(new BallotChoice(ballotTest, choiceTest2));
        ballotTest.addChoice(new BallotChoice(ballotTest, choiceTest3));
        entityManager.persist(ballotTest);
        entityManager.flush();


        transaction.commit();
        entityManager.close();
    }*/
















    @Test
    public void removeChoice()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.remove(getChoiceTest());

        entityManager.flush();
        transaction.commit();
        entityManager.close();
    }

    @Test
    public void removeVote()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.remove(getVoteTest());

        entityManager.flush();
        transaction.commit();
        entityManager.close();
    }

    @Test
    public void removeUser()
    {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.remove(getUserTest());

        entityManager.flush();
        transaction.commit();
        entityManager.close();
    }
}
