package fr.univtln.mapare.dao;

import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.exceptions.ForbiddenException;
import fr.univtln.mapare.model.*;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.Response;

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

    public List<Ballot> findByVoter(User voter) {
        return entityManager.createNamedQuery("Ballot.findByVoter", Ballot.class).setParameter("voter", voter).getResultList();
    }

    public List<Ballot> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Ballot.findByVote", Ballot.class).setParameter("vote", vote).getResultList();
    }

    @Override
    public void persist(Ballot ballot) throws BusinessException {
        VotedVoteDAO votedVoteDAO = VotedVoteDAO.of(entityManager);
        if (votedVoteDAO.findByUserVote(ballot.getVoter(), ballot.getVote()) == null) { // Check if have already voted
            if (ballot.getVote().getAnonymous()) // Check if anonymous
                ballot.setVoter(null);
            super.persist(ballot);
            votedVoteDAO.persist(VotedVote.builder().user(ballot.getVoter()).vote(ballot.getVote()).build());
        } else
            throw new ConflictException("User has already voted");
    }

}