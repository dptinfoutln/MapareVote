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
    public static Controller<Vote> PublicVotes = new Controller<>();
    public static Controller<Vote> PrivateVotes = new Controller<>();

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
                if (!PublicVotes.mapContainsKey(v.getId()))
                    PublicVotes.mapAdd(v.getId(), v);
            } for (Vote v : u.getPrivateVoteList()) {
                if (!PrivateVotes.mapContainsKey(v.getId()))
                    PrivateVotes.mapAdd(v.getId(), v);
//            } for (VotedVote vv : u.getVotedVotes()) {
//                if (!PublicVotes.mapContainsKey(vv.getVote().getId()))
//                    PublicVotes.mapAdd(vv.getVote().getId(), vv.getVote());
            }
        }
    }

    public static void loadPublicVotes() {
        List<Vote> templist = Controllers.getEntityManager().createNamedQuery("Vote.findPublic").getResultList();
//            Users.setMap(templist.stream().collect(Collectors.toMap(User::getId, user -> user)));
        for (Vote v : templist) {
            if (!PublicVotes.mapContainsKey(v.getId()))
                PublicVotes.mapAdd(v.getId(), v);
            for (User u : v.getMembers()) {
                if (!Users.mapContainsKey(u.getId()))
                    Users.mapAdd(u.getId(), u);
                for (Vote v2 : u.getPrivateVoteList()) {
                    if (!PrivateVotes.mapContainsKey(v2.getId()))
                        PrivateVotes.mapAdd(v2.getId(), v2);
                }
            }
            if (!Users.mapContainsKey(v.getVotemaker().getId()))
                Users.mapAdd(v.getVotemaker().getId(), v.getVotemaker());
        }
    }
}
