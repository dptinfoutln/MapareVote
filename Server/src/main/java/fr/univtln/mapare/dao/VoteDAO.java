package fr.univtln.mapare.dao;

import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VoteDAO extends GenericIdDAO<Vote> {

    public static VoteDAO of(EntityManager entityManager) {
        return new VoteDAO(entityManager);
    }

    private VoteDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Vote> findAll() {
        return entityManager.createNamedQuery("Vote.findAll", Vote.class).getResultList();
    }

    public List<Vote> findByVotemaker(User votemaker) {
        return entityManager.createNamedQuery("Vote.findByVotemaker", Vote.class).setParameter("votemaker", votemaker).getResultList();
    }

    public List<Vote> findAllPublic() {
        return entityManager.createNamedQuery("Vote.findPublic", Vote.class).getResultList();
    }

    public List<Vote> findPrivateByUser(User user) {
        return entityManager.createNamedQuery("Vote.findPrivateByUser", Vote.class).setParameter("user", user).getResultList();
    }

    @Override
    public void persist(Vote entity) {
        super.persist(entity);
    }
}
