package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class VoteUtils {

    public static VoteResults voteResultsOf(Vote vote) {
        return new VoteResults(vote);
    }

    public static class VoteResults implements Runnable {
        private Vote vote;

        public VoteResults(Vote vote) {
            this.vote = vote;
        }

        @Override
        public void run() {

        }

        public void calculateResults() {
            switch (vote.getAlgo()) {
                case "majority":
                case "borda":
                    Map<Choice, Integer> countmap = new HashMap<>();
                    for (Choice c : vote.getChoices())
                        countmap.put(c, 0);
                    for (Ballot b : vote.getBallots()) {
                        for (BallotChoice bc : b.getChoices()) {
                            countmap.put(bc.getChoice(), countmap.get(bc.getChoice()) + bc.getWeight());
                        }
                    }
                    List<VoteResult> resultList = new ArrayList<>();
                    for (Choice c : vote.getChoices()) {
                        resultList.add(new VoteResult(c, countmap.get(c), vote));
                    }
                    vote.setResultList(resultList);
                    break;
                case "STV":
                default:
                    vote.setResultList(null);
                    break;
            }
        }
    }
}
