package fr.univtln.mapare.dao;


import fr.univtln.mapare.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");

    public static void main(String[] args) {

        EntityManager entityManager = EMF.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();


        /*
        User personne = new User("test@test.com",
                "TEST",
                "test",
                "AZERTY",
                true,
                false,
                false,
                false);

        entityManager.persist(personne);

        entityManager.flush();

        transaction.commit();



        List<User> userList = entityManager.createNamedQuery("findUserWithId")
                .setParameter("id", 1)
                .getResultList();

        for (User u : userList) {
            System.out.println(u.getLastname());
        }




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
                .setParameter("vote", )
                .getResultList();

        // Find public votes
        List<Vote> publicVoteList = entityManager.createNamedQuery("Vote.findPublic").getResultList();
        System.out.println(publicVoteList.get(0).getLabel());


        // Find Vote by id
        List<Vote> voteList = entityManager.createNamedQuery("Vote.findById")
                .setParameter("id", 1)
                .getResultList();

        Vote vote1 = voteList.get(0);
        System.out.println("Id vote : " + vote1.getId());


        // Find Ballot by Vote
        List<Ballot> ballotList = entityManager.createNamedQuery("Ballot.findByVote")
                .setParameter("vote", vote1)
                .getResultList();

        Ballot ballot1 = ballotList.get(0);
        System.out.println("Id ballot : " + ballot1.getId());

        // Find Choices of a ballot
        List<BallotChoice> ballotChoiceList = entityManager.createNamedQuery("Choices.findByBallot")
                .setParameter("ballot", ballot1)
                .getResultList();

        for (BallotChoice B : ballotChoiceList) {
            List<String> names = B.getChoice().getNames();
            for (String c : names) {
                System.out.println("choix : " + c);
            }
        };




        // Find Vote by id
        Vote vote = (Vote) entityManager.createNamedQuery("Vote.findById")
                .setParameter("id", 801)
                .getResultList().get(0);

        // Find an user by id
        User personne = (User) entityManager.createNamedQuery("User.findById")
                .setParameter("id", 551)
                .getResultList().get(0);

        //BallotDAO.persist(Arrays.asList(new Choice(Arrays.asList("oui_test", "non_test", "blanc_test"), vote)), personne, vote, LocalDateTime.now());
        */


//        User personneTest = User.builder().email("test@test.com").lastname( "TEST").firstname("test").password("MotDePasseSecret").build();
//        System.out.println(personneTest.toString());
//        entityManager.persist(personneTest);
//        entityManager.flush();
//        transaction.commit();
//        entityManager.close();

        User userTest = (User) entityManager.createNamedQuery("User.findByEmail").setParameter("email", "test@test.com").getResultList().get(0);

        // Test persist pour vote
        Vote voteTest = Vote.builder().label("3.0 Les tests sont-ils toujours utiles ?").
                startDate(LocalDate.now()).
                anonymous(false).
                algo("algoDeTest").
                votemaker(userTest).build();
        System.out.println(voteTest);
        VoteDAO.of(entityManager).persist(voteTest);
        transaction.commit();
        System.out.println(voteTest);

//        Vote voteTest = (Vote) entityManager.createNamedQuery("Vote.findById").setParameter("id", 5).getSingleResult();

        //voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Oui_Test", "OuiBis_Test")).build());
//        voteTest.setChoices(new ArrayList<>());
//        voteTest.setLabel("2.0 Les tests sont-ils toujours utiles ?");
//        entityManager.merge(voteTest);
//        transaction.commit();
//        entityManager.close();

        //Vote voteTest = new Vote("Les tests sont-ils toujours utiles ?", LocalDate.now(), null, "algoDeTest", false, false, userTest);
        //System.out.println(voteTest);
    }

}

