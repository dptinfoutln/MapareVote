package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VotedVote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Controllers {
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");
    private static EntityManager entityManager = null;
    public static Controller<User> Users = new Controller<>();
    private static Controller<Vote> Votes = new Controller<>();

    private Controllers() {}

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static Boolean isOpen() {
        return entityManager != null;
    }

    public static void init() {
        if (entityManager == null)
            entityManager = EMF.createEntityManager();
    }

    public static void close() {
        entityManager.close();
        entityManager = null;
    }

    public static void loadUsers() {
        List<User> templist = Controllers.getEntityManager().createNamedQuery("User.findAll").getResultList();
//            Users.setMap(templist.stream().collect(Collectors.toMap(User::getId, user -> user)));
        for (User u : templist) {
            if (!Users.mapContainsKey(u.getId()))
                Users.mapAdd(u.getId(), u);
            for (Vote v : u.getStartedVotes()) {
                if (!Votes.mapContainsKey(v.getId()))
                    Votes.mapAdd(v.getId(), v);
            } for (Vote v : u.getPrivateVoteList()) {
                if (!Votes.mapContainsKey(v.getId()))
                    Votes.mapAdd(v.getId(), v);
            } for (VotedVote vv : u.getVotedVotes()) {
                if (!Votes.mapContainsKey(vv.getVote().getId()))
                    Votes.mapAdd(vv.getVote().getId(), vv.getVote());
            }
        }
    }

    public static void loadPublicVotes() {
        List<Vote> templist = Controllers.getEntityManager().createNamedQuery("Vote.findPublic").getResultList();
//            Users.setMap(templist.stream().collect(Collectors.toMap(User::getId, user -> user)));
        for (Vote v : templist) {
            if (!Votes.mapContainsKey(v.getId()))
                Votes.mapAdd(v.getId(), v);
            for (User u : v.getMembers()) {
                if (!Users.mapContainsKey(u.getId()))
                    Users.mapAdd(u.getId(), u);
            }
            if (!Users.mapContainsKey(v.getVotemaker().getId()))
                Users.mapAdd(v.getVotemaker().getId(), v.getVotemaker());
        }
    }
}
