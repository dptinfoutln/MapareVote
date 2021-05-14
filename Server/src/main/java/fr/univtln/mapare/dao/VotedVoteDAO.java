package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VotedVote;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VotedVoteDAO extends DAO<VotedVote> {

    public static VotedVoteDAO of(EntityManager entityManager) {
        return new VotedVoteDAO(entityManager);
    }

    private VotedVoteDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<VotedVote> findAll() {
        return entityManager.createNamedQuery("VotedVotes.findAll", VotedVote.class).getResultList();
    }

    public VotedVote findByToken(String token) {
        List<VotedVote> votedVoteList = entityManager.createNamedQuery("VotedVotes.findByToken", VotedVote.class).setParameter("token", token).getResultList();
        return votedVoteList.isEmpty() ? null : votedVoteList.get(0);
    }

    public VotedVote findByUserVote(User user, Vote vote) {
        List<VotedVote> votedVoteList = entityManager.createNamedQuery("VotedVotes.findByUser&Vote", VotedVote.class).setParameter("user", user).setParameter("vote", vote).getResultList();
        return votedVoteList.isEmpty() ? null : votedVoteList.get(0);
    }
}
