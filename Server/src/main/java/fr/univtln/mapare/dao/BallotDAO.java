package fr.univtln.mapare.dao;

import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.model.Ballot;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.model.VotedVote;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * The type Ballot dao.
 */
public class BallotDAO extends GenericIdDAO<Ballot> {

    /**
     * Of ballot dao.
     *
     * @param entityManager the entity manager
     * @return the ballot dao
     */
    public static BallotDAO of(EntityManager entityManager) {
        return new BallotDAO(entityManager);
    }

    private BallotDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Ballot> findAll() {
        return entityManager.createNamedQuery("Ballot.findAll", Ballot.class).getResultList();
    }

    /**
     * Find all list.
     *
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the list
     */
    public List<Ballot> findAll(int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Ballot.findAll", Ballot.class)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();

    }

    /**
     * Find by voter list.
     *
     * @param voter the voter
     * @return the list
     */
    public List<Ballot> findByVoter(User voter) {
        return entityManager.createNamedQuery("Ballot.findByVoter", Ballot.class).setParameter("voter", voter).getResultList();
    }

    /**
     * Find by voter list.
     *
     * @param voter     the voter
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the list
     */
    public List<Ballot> findByVoter(User voter, int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Ballot.findByVoter", Ballot.class)
                .setParameter("voter", voter)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();
    }

    /**
     * Find by vote list.
     *
     * @param vote the vote
     * @return the list
     */
    public List<Ballot> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Ballot.findByVote", Ballot.class).setParameter("vote", vote).getResultList();
    }

    /**
     * Find by vote list.
     *
     * @param vote      the vote
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the list
     */
    public List<Ballot> findByVote(Vote vote, int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Ballot.findByVote", Ballot.class)
                .setParameter("vote", vote)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex - 1) * pageSize)
                .getResultList();
    }

    /**
     * Find by vote by voter ballot.
     *
     * @param vote  the vote
     * @param voter the voter
     * @return the ballot
     */
    public Ballot findByVoteByVoter(Vote vote, User voter) {
        return entityManager.createNamedQuery("Ballot.findByVoteByVoter", Ballot.class)
                .setParameter("vote", vote)
                .setParameter("voter", voter)
                .getSingleResult();
    }

    @Override
    public void persist(Ballot ballot) throws BusinessException {
        if (VotedVoteDAO.of(entityManager).findByUserVote(ballot.getVoter(), ballot.getVote()) == null) { // Check if have already voted
            ballot.getVoter().addVotedVote(VotedVote.builder().user(ballot.getVoter()).vote(ballot.getVote()).build());
            UserDAO.of(entityManager).update(ballot.getVoter());
            if (ballot.getVote().isAnonymous()) // Check if anonymous
                ballot.setVoter(null);
            super.persist(ballot);
        } else
            throw new ConflictException("User has already voted");
    }

}
