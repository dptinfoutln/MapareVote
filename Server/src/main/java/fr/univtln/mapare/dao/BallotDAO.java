package fr.univtln.mapare.dao;

import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.model.*;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BallotDAO extends GenericIdDAO<Ballot> {

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

    public List<Ballot> findAll(int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Ballot.findAll", Ballot.class)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex-1) * pageSize)
                .getResultList();

    }

    public List<Ballot> findByVoter(User voter) {
        return entityManager.createNamedQuery("Ballot.findByVoter", Ballot.class).setParameter("voter", voter).getResultList();
    }

    public List<Ballot> findByVoter(User voter, int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Ballot.findByVoter", Ballot.class)
                .setParameter("voter", voter)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex-1) * pageSize)
                .getResultList();
    }

    public List<Ballot> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Ballot.findByVote", Ballot.class).setParameter("vote", vote).getResultList();
    }

    public List<Ballot> findByVote(Vote vote, int pageIndex, int pageSize) {
        return entityManager.createNamedQuery("Ballot.findByVote", Ballot.class)
                .setParameter("vote", vote)
                .setMaxResults(pageSize)
                .setFirstResult((pageIndex-1) * pageSize)
                .getResultList();
    }

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
