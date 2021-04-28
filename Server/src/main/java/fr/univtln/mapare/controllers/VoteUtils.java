package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.*;
import org.glassfish.grizzly.utils.Pair;

import java.util.*;

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
            calculateResults();
        }

        public void calculateResults() {
            List<VoteResult> resultList;
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
                    resultList = new ArrayList<>();
                    for (Choice c : vote.getChoices()) {
                        resultList.add(new VoteResult(c, countmap.get(c), vote));
                    }
                    vote.setResultList(resultList);
                    break;
                case "STV":
                    resultList = new ArrayList<>();

                    if (vote.getMaxChoices() == 1) {
                        List<List<Choice>> collated = new ArrayList<>();
                        List<Choice> temp;
                        for (Ballot b : vote.getBallots()) {
                            b.getChoices().sort(Comparator.comparingInt(BallotChoice::getWeight));
                            temp = new ArrayList<>();
                            for (BallotChoice bc : b.getChoices()) {
                                temp.add(bc.getChoice());
                            }
                            collated.add(temp);
                        }
                        Map<Choice, Integer> voteCounting = new HashMap<>();
                        for (Choice c : vote.getChoices())
                            voteCounting.put(c, 0);
                        for (List<Choice> lc : collated) {
                            voteCounting.put(lc.get(0), voteCounting.get(lc.get(0)) + 1);
                        }

                        for (Choice c : vote.getChoices()) {
                            if (voteCounting.get(c) > vote.getBallots().size() / 2)
                                resultList.add(new VoteResult(c, 1, vote));
                        }
                    }
                    else {
                        //TODO: later
                    }

                    vote.setResultList(resultList);
                    break;
                default:
                    vote.setResultList(null);
                    break;
            }
        }
    }
}
