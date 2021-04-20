package fr.univtln.mapare.dao;

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

    public List<Ballot> findByVoter(User voter) {
        return entityManager.createNamedQuery("Ballot.findByVoter", Ballot.class).setParameter("voter", voter).getResultList();
    }

    public List<Ballot> findByVote(Vote vote) {
        return entityManager.createNamedQuery("Ballot.findByVote", Ballot.class).setParameter("vote", vote).getResultList();
    }

    public Ballot findByVoteByVoter(Vote vote, User voter) {
        return entityManager.createNamedQuery("Ballot.findByVoteByVoter", Ballot.class)
                .setParameter("vote", vote)
                .setParameter("voter", voter)
                .getSingleResult();
    }

    @Override
    public void persist(Ballot ballot) {
        VotedVoteDAO votedVoteDAO = VotedVoteDAO.of(entityManager);

        if (votedVoteDAO.findByUserVote(ballot.getVoter(), ballot.getVote()) == null) {
            super.persist(ballot);
            votedVoteDAO.persist(VotedVote.builder().user(ballot.getVoter()).vote(ballot.getVote()).build());
        }
        //TODO (else) exception déjà voté (voir si ça intéresse François)
        //TODO anonymous ballot (ballot toujours avec voter / verif vote si anonymous
        //TODO check si le vote n'est pas deleted
    }

}
