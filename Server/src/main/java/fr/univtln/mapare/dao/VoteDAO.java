package fr.univtln.mapare.dao;


import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import fr.univtln.mapare.resources.VoteQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
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

    public List<Vote> findAll(VoteQuery voteQuery) {
        return filterAndSortList("Vote.findAll", null, null , voteQuery);

    }

    public List<Vote> findByVotemaker(User votemaker) {
        return entityManager.createNamedQuery("Vote.findByVotemaker", Vote.class).setParameter("votemaker", votemaker).getResultList();
    }

    public List<Vote> findByVotemaker(User votemaker, VoteQuery voteQuery) {
        return filterAndSortList("Vote.findByVotemaker", votemaker, "votemaker", voteQuery);
    }

    public List<Vote> findByVoter(User voter, VoteQuery voteQuery) {
        return filterAndSortList("Vote.findByVoter", voter, "voter", voteQuery);
    }

    public List<Vote> findAllPublic() {
        return entityManager.createNamedQuery("Vote.findPublic", Vote.class).getResultList();
    }

    public List<Vote> findAllPublic(VoteQuery voteQuery) {
        return filterAndSortList("Vote.findPublic", null, null, voteQuery);
    }

    public List<Vote> findPrivateByUser(User user) {
        return entityManager.createNamedQuery("Vote.findPrivateByUser", Vote.class).setParameter("user", user).getResultList();
    }

    public List<Vote> findPrivateByUser(User user, VoteQuery voteQuery) {
        return filterAndSortList("Vote.findPrivateByUser", user, "user", voteQuery);
    }

    private List<Vote> filterAndSortList(String namedQuery, User parameter, String role, VoteQuery voteQuery) {
        TypedQuery<Vote> query = entityManager.createNamedQuery(namedQuery, Vote.class);
        if (parameter != null)
            query.setParameter(role, parameter);
        Stream<Vote> voteStream = query.getResultStream();
        if (voteQuery.getAlgoname() != null)
            voteStream = voteStream.filter(v -> v.getAlgo().equalsIgnoreCase(voteQuery.getAlgoname()));
        if (voteQuery.isOpen())
            voteStream = voteStream.filter(v ->
                    (v.getStartDate().isBefore(LocalDate.now()) || v.getStartDate().isEqual(LocalDate.now())) &&
                    (v.getEndDate() == null || (v.getEndDate() != null && v.getEndDate().isAfter(LocalDate.now()))));
        if (voteQuery.getExactmatch() != null)
            voteStream = voteStream.filter(v -> v.getLabel().toUpperCase()
                    .contains(voteQuery.getExactmatch().toUpperCase()));
        if (voteQuery.getPrefixmatch() != null)
            voteStream = voteStream.filter(v -> v.getLabel().toUpperCase()
                    .startsWith(voteQuery.getPrefixmatch().toUpperCase()));
        if (voteQuery.getSuffixmatch() != null)
            voteStream = voteStream.filter(v -> v.getLabel().toUpperCase()
                    .endsWith(voteQuery.getSuffixmatch().toUpperCase()));

        voteStream = voteStream.sorted(Collections.reverseOrder());

        if (voteQuery.getSortkey() != null) {
            Comparator<Vote> comparator = null;
            switch (voteQuery.getSortkey().toUpperCase()) {
                case "NAME":
                    comparator = Comparator.comparing(Vote::getLabel);
                    break;
                case "VOTES":
                    comparator = Comparator.comparingInt(vote -> vote.getBallots().size());
                    break;
                case "STARTDATE":
                    comparator = Comparator.comparing(Vote::getStartDate);
                    break;
                default:
                    break;
            }

            if (comparator != null) {
                if ("desc".equalsIgnoreCase(voteQuery.getOrder()))
                    comparator = comparator.reversed();
                voteStream = voteStream.sorted(comparator);
            }
        }
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
