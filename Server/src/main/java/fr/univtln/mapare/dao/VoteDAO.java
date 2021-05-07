package fr.univtln.mapare.dao;


import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Vote> findAll(int pageIndex, int pageSize, String exactmatch, String prefixmatch,
                              String suffixmatch, String algoname) {
        return filterAndSortList("Vote.findAll", null, null , pageIndex, pageSize, exactmatch,
                prefixmatch, suffixmatch, algoname);

    }

    public List<Vote> findByVotemaker(User votemaker) {
        return entityManager.createNamedQuery("Vote.findByVotemaker", Vote.class).setParameter("votemaker", votemaker).getResultList();
    }

    public List<Vote> findByVotemaker(User votemaker, int pageIndex, int pageSize, String exactmatch, String prefixmatch,
                                      String suffixmatch, String algoname) {
        return filterAndSortList("Vote.findByVotemaker", votemaker, "votemaker", pageIndex, pageSize, exactmatch,
                prefixmatch, suffixmatch, algoname);
    }

    public List<Vote> findAllPublic() {
        return entityManager.createNamedQuery("Vote.findPublic", Vote.class).getResultList();
    }

    public List<Vote> findAllPublic(int pageIndex, int pageSize, String exactmatch, String prefixmatch,
                                    String suffixmatch, String algoname) {
        return filterAndSortList("Vote.findPublic", null, null, pageIndex, pageSize, exactmatch,
                prefixmatch, suffixmatch, algoname);
    }

    public List<Vote> findPrivateByUser(User user) {
        return entityManager.createNamedQuery("Vote.findPrivateByUser", Vote.class).setParameter("user", user).getResultList();
    }

    public List<Vote> findPrivateByUser(User user, int pageIndex, int pageSize, String exactmatch, String prefixmatch,
                                        String suffixmatch, String algoname) {
        return filterAndSortList("Vote.findPrivateByUser", user, "user", pageIndex, pageSize, exactmatch,
                prefixmatch, suffixmatch, algoname);
    }

    private List<Vote> filterAndSortList(String namedQuery, User parameter, String role, int pageIndex, int pageSize,
                                         String exactmatch, String prefixmatch, String suffixmatch, String algoname) {
        TypedQuery<Vote> query = entityManager.createNamedQuery(namedQuery, Vote.class);
        if (parameter != null)
            query.setParameter(role, parameter);
        Stream<Vote> voteStream = query.getResultStream();
        if (algoname != null)
            voteStream = voteStream.filter(v -> v.getAlgo().equals(algoname));
        if (exactmatch != null)
            voteStream = voteStream.filter(v -> v.getLabel().contains(exactmatch));
        if (prefixmatch != null)
            voteStream = voteStream.filter(v -> v.getLabel().startsWith(prefixmatch));
        if (suffixmatch != null)
            voteStream = voteStream.filter(v -> v.getLabel().endsWith(suffixmatch));

        voteStream = voteStream.skip((long) pageSize * (pageIndex - 1)).limit(pageSize);
        return voteStream.collect(Collectors.toList());
    }

    @Override
    public void persist(Vote entity) throws BusinessException {
        if (entity.getVotemaker() == null)
            throw new ConflictException("No votemaker.");
        entity.getVotemaker().addStartedVote(entity);
        super.persist(entity);
    }
}
