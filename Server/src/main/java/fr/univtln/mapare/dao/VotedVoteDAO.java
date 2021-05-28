package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VotedVote;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * The type Voted vote dao.
 */
public class VotedVoteDAO extends DAO<VotedVote> {

    /**
     * Of voted vote dao.
     *
     * @param entityManager the entity manager
     * @return the voted vote dao
     */
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

    /**
     * Find by token voted vote.
     *
     * @param token the token
     * @return the voted vote
     */
    public VotedVote findByToken(String token) {
        List<VotedVote> votedVoteList = entityManager.createNamedQuery("VotedVotes.findByToken", VotedVote.class).setParameter("token", token).getResultList();
        return votedVoteList.isEmpty() ? null : votedVoteList.get(0);
    }

    /**
     * Find by user vote voted vote.
     *
     * @param user the user
     * @param vote the vote
     * @return the voted vote
     */
    public VotedVote findByUserVote(User user, Vote vote) {
        List<VotedVote> votedVoteList = entityManager.createNamedQuery("VotedVotes.findByUser&Vote", VotedVote.class).setParameter("user", user).setParameter("vote", vote).getResultList();
        return votedVoteList.isEmpty() ? null : votedVoteList.get(0);
    }

    /**
     * Find by vote list.
     *
     * @param vote the vote
     * @return the list
     */
    public List<VotedVote> findByVote(Vote vote) {
        List<VotedVote> votedVoteList = entityManager.createNamedQuery("VotedVotes.findByVote", VotedVote.class).setParameter("vote", vote).getResultList();
        return votedVoteList.isEmpty() ? null : votedVoteList;
    }
}
