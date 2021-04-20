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


//        User personneTest = User.builder().email("glotin@mail.net").lastname( "GLOTIN").firstname("Hervé").password("J'aimeLesDauphins").build();
//        User personneTest = User.builder().email("test@mail.com").lastname( "TEST").firstname("test").password("MotDePAsseSecret").build();
//        System.out.println(personneTest);
//        UserDAO.of(entityManager).persist(personneTest);

        User userTest = UserDAO.of(entityManager).findById(2);

        // Test persist pour vote
//        Vote voteTest = Vote.builder().label("Quelle jour pour la réu de la semaine prochaine?").
//                startDate(LocalDate.now()).
//                anonymous(false).
//                algo("algoDeTestBis").
//                votemaker(userTest).build();
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Lundi")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Mardi")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Mercredi")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("jeudi")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Vendredi")).build());
//        voteTest.addMember(userTest);
//        System.out.println(voteTest);
//        VoteDAO.of(entityManager).persist(voteTest);
//        System.out.println(voteTest);

        Vote voteTest = VoteDAO.of(entityManager).findById(2);
//        voteTest.addMember(userTest);
//        VoteDAO.of(entityManager).update(voteTest);

//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Oui_Test")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Non_Test")).build());
//        entityManager.merge(voteTest);
//        transaction.commit();
//        entityManager.close();

        //Vote voteTest = new Vote("Les tests sont-ils toujours utiles ?", LocalDate.now(), null, "algoDeTest", false, false, userTest);
        //System.out.println(voteTest);

        // Exemple of ballot
        Choice myChoice = ChoiceDAO.of(entityManager).findByVote(voteTest).get(3);

        BallotDAO ballotDAO = BallotDAO.of(entityManager);
        Ballot myBallot = Ballot.builder().date(LocalDateTime.now()).voter(userTest).vote(voteTest).build();
        myBallot.addChoice(BallotChoice.builder().ballot(myBallot).choice(myChoice).build());
        System.out.println(myBallot);
        ballotDAO.persist(myBallot);

        //System.out.println(UserDAO.of(entityManager).findById(1));
//        UserDAO.of(entityManager).findAll();

    }

}

