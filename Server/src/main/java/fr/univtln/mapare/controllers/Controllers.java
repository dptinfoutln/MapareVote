package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VotedVote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controllers {
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("maparevotedb");
    private static EntityManager entityManager = null;
//    public static Controller<User> Users = new Controller<>();
//    public static Controller<Vote> PublicVotes = new Controller<>();
//    public static Controller<Vote> PrivateVotes = new Controller<>();
//    public static Controller<Vote> Votes = new Controller<>();

    private Controllers() {}

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static boolean isOpen() {
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

    public static List executeRequest(String request) {
        return entityManager.createNamedQuery(request).getResultList();
    }

    public static List executeParamRequest(String request, String param, int value) {
        return entityManager.createNamedQuery(request).setParameter(param, value).getResultList();
    }

    public static List executeParamRequest(String request, String param, String value) {
        return entityManager.createNamedQuery(request).setParameter(param, value).getResultList();
    }

//    public static void loadUsers() {
//        storeUsers(Controllers.getEntityManager().createNamedQuery("User.findAll").getResultList());
//    }

//    private static void storeUsers(List<User> list) {
//        for (User u : list) {
//            if (!Users.mapContainsKey(u.getId()))
//                Users.mapAdd(u.getId(), u);
//            for (Vote v : u.getStartedVotes()) {
//                if (!PublicVotes.mapContainsKey(v.getId())) {
//                    PublicVotes.mapAdd(v.getId(), v);
//                    Votes.mapAdd(v.getId(), v);
//                }
//            } for (Vote v : u.getPrivateVoteList()) {
//                if (!PrivateVotes.mapContainsKey(v.getId())) {
//                    PrivateVotes.mapAdd(v.getId(), v);
//                    Votes.mapAdd(v.getId(), v);
//                }
//            } for (VotedVote vv : u.getVotedVotes()) {
//                if (!PublicVotes.mapContainsKey(vv.getVote().getId())) {
//                    Votes.mapAdd(vv.getVote().getId(), vv.getVote());
//                    if (u.getPrivateVoteList().contains(vv.getVote()))
//                        PrivateVotes.mapAdd(vv.getVote().getId(), vv.getVote());
//                    else
//                        PublicVotes.mapAdd(vv.getVote().getId(), vv.getVote());
//                }
//            }
//        }
//    }
//
//    public static void loadVotes() {
//        List<Vote> templist = Controllers.getEntityManager().createNamedQuery("Vote.findPublic").getResultList();
//        Map<Integer, User> usermap = new HashMap<>();
//        for (Vote v : templist) {
////            if (!PublicVotes.mapContainsKey(v.getId())) {
////                PublicVotes.mapAdd(v.getId(), v);
////                Votes.mapAdd(v.getId(), v);
////            }
////            if (!Users.mapContainsKey(v.getVotemaker().getId()))
////                Users.mapAdd(v.getVotemaker().getId(), v.getVotemaker());
//            if(!Users.mapContainsKey(v.getVotemaker().getId()) && !usermap.containsKey(v.getVotemaker().getId()))
//                usermap.put(v.getVotemaker().getId(), v.getVotemaker());
//        }
//        templist = Controllers.getEntityManager().createNamedQuery("Vote.findPrivate").getResultList();
//        for (Vote v : templist) {
////            if (!PrivateVotes.mapContainsKey(v.getId())) {
////                PrivateVotes.mapAdd(v.getId(), v);
////                Votes.mapAdd(v.getId(), v);
////            }
////            if (!Users.mapContainsKey(v.getVotemaker().getId()))
////                Users.mapAdd(v.getVotemaker().getId(), v.getVotemaker());
//            if (!Users.mapContainsKey(v.getVotemaker().getId()) && !usermap.containsKey(v.getVotemaker().getId()))
//                usermap.put(v.getVotemaker().getId(), v.getVotemaker());
//            for (User u : v.getMembers()) {
//                if (!Users.mapContainsKey(u.getId()) && !usermap.containsKey(u.getId()))
//                    usermap.put(u.getId(), u);
//            }
//        }
//        storeUsers(new ArrayList<>(usermap.values()));
//    }
}
