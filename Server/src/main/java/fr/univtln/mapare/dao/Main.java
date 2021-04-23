package fr.univtln.mapare.dao;


import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.extern.java.Log;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log
public class Main {
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");

    public static void main(String[] args) throws BusinessException {

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


//        User userTest = User.builder().email("glotin@mail.net").lastname( "GLOTIN").firstname("Hervé").password("mdp").build();
//        User userTest = User.builder().email("test@mail.com").lastname( "TEST").firstname("test").password("MotDePAsseSecret").build();
//        System.out.println(personneTest);
//        UserDAO.of(entityManager).persist(userTest);
//
//        User userTest = UserDAO.of(entityManager).findById(1);
//
//        // Test persist pour vote
//        Vote voteTest = Vote.builder().label("strawpoll 4").
//                startDate(LocalDate.now()).
//                anonymous(false).
//                algo("algoDeTest").
//                votemaker(userTest).build();
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Oui")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Non")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Blanc")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("jeudi")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Vendredi")).build());
//        voteTest.addMember(userTest);
//        System.out.println(voteTest);
//        VoteDAO.of(entityManager).persist(voteTest);
//        System.out.println(voteTest);

//        Vote voteTest = VoteDAO.of(entityManager).findById(2);
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
//        Choice myChoice = ChoiceDAO.of(entityManager).findByVote(voteTest).get(3);
//
//        BallotDAO ballotDAO = BallotDAO.of(entityManager);
//        Ballot myBallot = Ballot.builder().date(LocalDateTime.now()).voter(userTest).vote(voteTest).build();
//        myBallot.addChoice(BallotChoice.builder().ballot(myBallot).choice(myChoice).build());
//        System.out.println(myBallot);
//        ballotDAO.persist(myBallot);

        //System.out.println(UserDAO.of(entityManager).findById(1));
//        UserDAO.of(entityManager).findAll();

//        Vote vote = VoteDAO.of(entityManager).findById(3);
//        System.out.println(vote);
//        vote.setResultList(Arrays.asList(new VoteResult(ChoiceDAO.of(entityManager).findById(7), 100, vote)));
//        VoteDAO.of(entityManager).update(vote);
//        entityManager.getTransaction().begin();
//        entityManager.remove(vote);
//        entityManager.getTransaction().commit();


//        User userTest1 = User.builder().email("glotin@mail.com").lastname( "GLOTIN").firstname("Hervé").password("mdp").build();
//        User userTest2 = User.builder().email("test@mail.com").lastname( "TEST").firstname("test").password("mdp").build();
//        User userTest3 = User.builder().email("test1@mail.com").lastname( "Rayner").firstname("Daryl").password("mdp").build();
//        User userTest4 = User.builder().email("test2@mail.com").lastname( "Peters").firstname("Dario").password("mdp").build();
//        User userTest5 = User.builder().email("test3@mail.com").lastname( "Malone").firstname("Tiana").password("mdp").build();
//        User userTest6 = User.builder().email("test4@mail.com").lastname( "Higgins").firstname("Asif").password("mdp").build();
//        UserDAO userDAO = UserDAO.of(entityManager);
//        userDAO.persist(userTest1);
//        userDAO.persist(userTest2);
//        userDAO.persist(userTest3);
//        userDAO.persist(userTest4);
//        userDAO.persist(userTest5);
//        userDAO.persist(userTest6);
//
//        // Test persist pour vote
//        Vote voteTest = Vote.builder().label("Aimez-vous les pâtes ?").
//                startDate(LocalDate.now()).
//                anonymous(false).
//                algo("majority").
//                votemaker(userTest3).build();
//        voteTest.setIntermediaryResult(true);
//        voteTest.setMaxChoices(1);
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Oui")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Non")).build());
//        voteTest.addChoice(Choice.builder().vote(voteTest).names(Arrays.asList("Blanc")).build());
//        VoteDAO.of(entityManager).persist(voteTest);
//
//        Vote voteTest1 = Vote.builder().label("Quelle jour pour la réunion de la semaine prochaine ?").
//                startDate(LocalDate.now()).
//                anonymous(true).
//                algo("borda").
//                votemaker(userTest1).build();
//        voteTest1.setIntermediaryResult(false);
//        voteTest1.setMaxChoices(3);
//        voteTest1.addChoice(Choice.builder().vote(voteTest1).names(Arrays.asList("Lundi")).build());
//        voteTest1.addChoice(Choice.builder().vote(voteTest1).names(Arrays.asList("Mardi")).build());
//        voteTest1.addChoice(Choice.builder().vote(voteTest1).names(Arrays.asList("Mercredi")).build());
//        voteTest1.addChoice(Choice.builder().vote(voteTest1).names(Arrays.asList("Jeudi")).build());
//        voteTest1.addChoice(Choice.builder().vote(voteTest1).names(Arrays.asList("Vendredi")).build());
//        voteTest1.addMember(userTest4);
//        voteTest1.addMember(userTest5);
//        voteTest1.addMember(userTest6);
//        VoteDAO.of(entityManager).persist(voteTest1);
//
//
//        Vote voteTest2 = Vote.builder().label("Quelle est votre doigt préféré ?").
//                startDate(LocalDate.now()).
//                anonymous(false).
//                algo("borda").
//                votemaker(userTest3).build();
//        voteTest2.setIntermediaryResult(true);
//        voteTest2.setMaxChoices(5);
//        voteTest2.addChoice(Choice.builder().vote(voteTest2).names(Arrays.asList("Pouce")).build());
//        voteTest2.addChoice(Choice.builder().vote(voteTest2).names(Arrays.asList("Index")).build());
//        voteTest2.addChoice(Choice.builder().vote(voteTest2).names(Arrays.asList("Majeur")).build());
//        voteTest2.addChoice(Choice.builder().vote(voteTest2).names(Arrays.asList("Annulaire")).build());
//        voteTest2.addChoice(Choice.builder().vote(voteTest2).names(Arrays.asList("Auriculaire")).build());
//        VoteDAO.of(entityManager).persist(voteTest2);
//
//        Vote voteTest3 = Vote.builder().label("Quelle est votre choix ?").
//                startDate(LocalDate.now()).
//                anonymous(false).
//                algo("majority").
//                votemaker(userTest1).build();
//        voteTest3.setIntermediaryResult(true);
//        voteTest3.setMaxChoices(3);
//        voteTest3.addChoice(Choice.builder().vote(voteTest3).names(Arrays.asList("Choix 1")).build());
//        voteTest3.addChoice(Choice.builder().vote(voteTest3).names(Arrays.asList("Choix 2")).build());
//        voteTest3.addChoice(Choice.builder().vote(voteTest3).names(Arrays.asList("Choix 3")).build());
//        voteTest3.addChoice(Choice.builder().vote(voteTest3).names(Arrays.asList("Choix 4")).build());
//        VoteDAO.of(entityManager).persist(voteTest3);
//
//        // Ballot
//        Choice myChoice = voteTest.getChoices().get(0);
//        Choice myChoice2 = voteTest.getChoices().get(1);
//        BallotDAO ballotDAO = BallotDAO.of(entityManager);
//        Ballot myBallot1 = Ballot.builder().date(LocalDateTime.now()).voter(userTest1).vote(voteTest).build();
//        Ballot myBallot2 = Ballot.builder().date(LocalDateTime.now()).voter(userTest2).vote(voteTest).build();
//        Ballot myBallot3 = Ballot.builder().date(LocalDateTime.now()).voter(userTest3).vote(voteTest).build();
//        Ballot myBallot4 = Ballot.builder().date(LocalDateTime.now()).voter(userTest4).vote(voteTest).build();
//        Ballot myBallot5 = Ballot.builder().date(LocalDateTime.now()).voter(userTest6).vote(voteTest).build();
//
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice2).build());
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice).build());
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice).build());
//
//        ballotDAO.persist(myBallot1);
//        ballotDAO.persist(myBallot2);
//        ballotDAO.persist(myBallot3);
//        ballotDAO.persist(myBallot4);
//        ballotDAO.persist(myBallot5);
//
//        // Ballot
//        myChoice = voteTest1.getChoices().get(0);
//        myChoice2 = voteTest1.getChoices().get(1);
//        Choice myChoice3 = voteTest1.getChoices().get(2);
//        Choice myChoice4 = voteTest1.getChoices().get(3);
//        Choice myChoice5 = voteTest1.getChoices().get(4);
//
//        myBallot1 = Ballot.builder().date(LocalDateTime.now()).voter(userTest4).vote(voteTest1).build();
//        myBallot2 = Ballot.builder().date(LocalDateTime.now()).voter(userTest5).vote(voteTest1).build();
//        myBallot3 = Ballot.builder().date(LocalDateTime.now()).voter(userTest6).vote(voteTest1).build();
//
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice).weight(3).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice4).weight(1).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice5).weight(2).build());
//
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice2).weight(3).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice).weight(1).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice3).weight(2).build());
//
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice5).weight(3).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice2).weight(1).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice3).weight(2).build());
//
//        ballotDAO.persist(myBallot1);
//        ballotDAO.persist(myBallot2);
//        ballotDAO.persist(myBallot3);
//
//        // Ballot
//        myChoice = voteTest2.getChoices().get(0);
//        myChoice2 = voteTest2.getChoices().get(1);
//        myChoice3 = voteTest2.getChoices().get(2);
//        myChoice4 = voteTest2.getChoices().get(3);
//        myChoice5 = voteTest2.getChoices().get(4);
//
//        myBallot1 = Ballot.builder().date(LocalDateTime.now()).voter(userTest1).vote(voteTest2).build();
//        myBallot2 = Ballot.builder().date(LocalDateTime.now()).voter(userTest2).vote(voteTest2).build();
//        myBallot3 = Ballot.builder().date(LocalDateTime.now()).voter(userTest3).vote(voteTest2).build();
//        myBallot4 = Ballot.builder().date(LocalDateTime.now()).voter(userTest4).vote(voteTest2).build();
//        myBallot5 = Ballot.builder().date(LocalDateTime.now()).voter(userTest5).vote(voteTest2).build();
//        Ballot myBallot6 = Ballot.builder().date(LocalDateTime.now()).voter(userTest6).vote(voteTest2).build();
//
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice3).weight(5).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice4).weight(4).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice2).weight(3).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice).weight(2).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice5).weight(1).build());
//
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice).weight(5).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice5).weight(4).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice3).weight(3).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice4).weight(2).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice2).weight(1).build());
//
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice).weight(5).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice4).weight(4).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice5).weight(3).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice2).weight(2).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice3).weight(1).build());
//
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice2).weight(5).build());
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice).weight(4).build());
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice3).weight(3).build());
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice4).weight(2).build());
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice5).weight(1).build());
//
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice).weight(5).build());
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice2).weight(4).build());
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice3).weight(3).build());
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice4).weight(2).build());
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice5).weight(1).build());
//
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice2).weight(5).build());
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice4).weight(4).build());
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice3).weight(3).build());
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice).weight(2).build());
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice5).weight(1).build());
//
//        ballotDAO.persist(myBallot1);
//        ballotDAO.persist(myBallot2);
//        ballotDAO.persist(myBallot3);
//        ballotDAO.persist(myBallot4);
//        ballotDAO.persist(myBallot5);
//        ballotDAO.persist(myBallot6);
//
//
//
//        // Ballot
//        myChoice = voteTest3.getChoices().get(0);
//        myChoice2 = voteTest3.getChoices().get(1);
//        myChoice3 = voteTest3.getChoices().get(2);
//        myChoice4 = voteTest3.getChoices().get(3);
//
//        myBallot1 = Ballot.builder().date(LocalDateTime.now()).voter(userTest1).vote(voteTest3).build();
//        myBallot2 = Ballot.builder().date(LocalDateTime.now()).voter(userTest2).vote(voteTest3).build();
//        myBallot3 = Ballot.builder().date(LocalDateTime.now()).voter(userTest3).vote(voteTest3).build();
//        myBallot4 = Ballot.builder().date(LocalDateTime.now()).voter(userTest4).vote(voteTest3).build();
//        myBallot5 = Ballot.builder().date(LocalDateTime.now()).voter(userTest5).vote(voteTest3).build();
//        myBallot6 = Ballot.builder().date(LocalDateTime.now()).voter(userTest6).vote(voteTest3).build();
//
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice3).weight(3).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice2).weight(2).build());
//        myBallot1.addChoice(BallotChoice.builder().ballot(myBallot1).choice(myChoice).weight(1).build());
//
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice3).weight(1).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice2).weight(2).build());
//        myBallot2.addChoice(BallotChoice.builder().ballot(myBallot2).choice(myChoice).weight(3).build());
//
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice2).weight(1).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice).weight(2).build());
//        myBallot3.addChoice(BallotChoice.builder().ballot(myBallot3).choice(myChoice3).weight(3).build());
//
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice2).weight(3).build());
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice3).weight(2).build());
//        myBallot4.addChoice(BallotChoice.builder().ballot(myBallot4).choice(myChoice).weight(1).build());
//
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice4).weight(1).build());
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice2).weight(2).build());
//        myBallot5.addChoice(BallotChoice.builder().ballot(myBallot5).choice(myChoice3).weight(3).build());
//
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice3).weight(3).build());
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice4).weight(2).build());
//        myBallot6.addChoice(BallotChoice.builder().ballot(myBallot6).choice(myChoice).weight(1).build());
//
//        ballotDAO.persist(myBallot1);
//        ballotDAO.persist(myBallot2);
//        ballotDAO.persist(myBallot3);
//        ballotDAO.persist(myBallot4);
//        ballotDAO.persist(myBallot5);
//        ballotDAO.persist(myBallot6);

        System.out.println(UserDAO.of(entityManager).findById(4).getVotedVotes().get(1).getVote().getLabel());
    }

}

