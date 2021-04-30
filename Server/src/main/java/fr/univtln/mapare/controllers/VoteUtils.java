package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.*;

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
            if (!vote.isPendingResult()) {
                vote.setPendingResult(true);
                List<VoteResult> resultList;
                int candidatecount = 1;
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

                        // Here we construct a list of ordered choices for each BallotChoice list depending on weight.
                        // Low weight = earlier choice, higher placement on list.
                        List<List<Choice>> collated = new ArrayList<>();
                        List<Choice> temp;
                        for (Ballot b : vote.getBallots()) {
                            // We sort them relative to weight.
                            b.getChoices().sort(Comparator.comparingInt(BallotChoice::getWeight));
                            temp = new ArrayList<>();
                            // Then put them in a temporary list.
                            for (BallotChoice bc : b.getChoices()) {
                                temp.add(bc.getChoice());
                            }
                            // Then add that list to the list list.
                            collated.add(temp);
                        }
                        Choice minchoice = null;


                        // IRV = 1 winner.
                        if (vote.getMaxChoices() == 1) {
                            Map<Choice, Integer> voteCounting = new HashMap<>();
                            int minval;

                            while (true) {
                                // We count the votes for each candidate.
                                for (Choice c : vote.getChoices())
                                    voteCounting.put(c, 0);
                                for (List<Choice> lc : collated) {
                                    voteCounting.put(lc.get(0), voteCounting.get(lc.get(0)) + 1);
                                }

                                // If we find a winner, we add it to the resultlist and end it here.
                                for (Choice c : vote.getChoices()) {
                                    if (voteCounting.get(c) > vote.getBallots().size() / 2) {
                                        resultList.add(new VoteResult(c, candidatecount, vote));
                                        break;
                                    }
                                }
                                if (resultList.size() == 1)
                                    break;

                                // Otherwise we search for the lowest ranked vote currently.
                                minval = vote.getBallots().size() + 1;
                                for (Choice c : vote.getChoices()) {
                                    if (voteCounting.get(c) != 0 && voteCounting.get(c) < minval) {
                                        minval = voteCounting.get(c);
                                        minchoice = c;
                                    }
                                    // Once found we remove it from everywhere.
                                    for (List<Choice> lc : collated) {
                                        lc.remove(minchoice);
                                    }
                                }
                            }
                        }

                        // STV = more than 1 winner
                        else {
                            Map<Choice, Double> countMap = new HashMap<>();
                            double minval;
                            boolean flag = false;
                            double ratio;
                            int votingforthis;
                            // We init the vote count for each candidate
                            for (Choice c : vote.getChoices())
                                countMap.put(c, 0.0);
                            for (List<Choice> lc : collated) {
                                countMap.put(lc.get(0), countMap.get(lc.get(0)) + 1);
                            }

                            while (resultList.size() != vote.getMaxChoices()) {
                                flag = false;
                                for (Choice c : vote.getChoices()) {
                                    // If we have someone who has enough votes, we can add him to the winners list.
                                    if (countMap.get(c) >= ((double) vote.getBallots().size()) / vote.getMaxChoices()) {
                                        flag = true;
                                        resultList.add(new VoteResult(c, candidatecount, vote));
                                        candidatecount++;
                                        // We calculate the ratio for vote redistribution
                                        ratio = 1 - ((double) vote.getBallots().size()) / vote.getMaxChoices() / countMap.get(c);
                                        // We redistribute the votes and remove the winner from every list
                                        redistribute(collated, c, countMap, ratio);
                                        break;
                                    }
                                }
                                // If flag is false, we have had no winner this round so we must eliminate a loser.
                                if (!flag) {
                                    // We find the candidate with the least amount of votes.
                                    minval = vote.getBallots().size() + 1.0;
                                    for (Choice c : vote.getChoices()) {
                                        if (countMap.get(c) != 0 && countMap.get(c) < minval) {
                                            minval = countMap.get(c);
                                            minchoice = c;
                                        }
                                    }
                                    // Once found we redistribute the votes and remove it from the pool.
                                    votingforthis = 0;

                                    // To do this we first count the number of individual votes for it.
                                    for (List<Choice> lc : collated) {
                                        if (lc.get(0).equals(minchoice))
                                            votingforthis++;
                                    }

                                    // Then we calculate the ratio for this vote.
                                    ratio = countMap.get(minchoice) / votingforthis;

                                    // Then proceed with the redistribution.
                                    redistribute(collated, minchoice, countMap, ratio);
                                }
                            }
                        }
                        vote.setResultList(resultList);
                        break;
                    default:
                        vote.setResultList(null);
                        break;
                }
                vote.setPendingResult(false);
            }
        }

        private void redistribute(List<List<Choice>> collated, Choice choice, Map<Choice, Double> countMap, double ratio) {
            for (List<Choice> lc : collated) {
                if (lc.get(0).equals(choice)) {
                    lc.remove(0);
                    countMap.put(lc.get(0), countMap.get(lc.get(0)) + ratio);
                }
                else
                    lc.remove(choice);
            }
            countMap.remove(choice);
        }
    }
}
