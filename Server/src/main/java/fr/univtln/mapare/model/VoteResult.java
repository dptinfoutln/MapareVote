package fr.univtln.mapare.model;

import org.glassfish.grizzly.utils.Pair;

import java.util.List;

public class VoteResult {
    private List<Pair<Choice, Integer>> resultList;
    private Vote vote;

    public VoteResult() {
    }

    public List<Pair<Choice, Integer>> getResultList() {
        return resultList;
    }

    public void setResultList(List<Pair<Choice, Integer>> resultList) {
        this.resultList = resultList;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }
}
