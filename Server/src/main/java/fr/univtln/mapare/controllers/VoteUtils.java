package fr.univtln.mapare.controllers;

import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.model.*;

import java.util.*;

/**
 * The type Vote utils.
 */
public abstract class VoteUtils {
    /**
     * Vote results of vote results.
     *
     * @param vote the vote
     * @return the vote results
     */
    public static VoteResults voteResultsOf(Vote vote) {
        return new VoteResults(vote);
    }

    private VoteUtils() {
    }

    /**
     * The type Vote results.
     */
    public static class VoteResults implements Runnable {
        private final Vote vote;

        /**
         * Instantiates a new Vote results.
         *
         * @param vote the vote
         */
        public VoteResults(Vote vote) {
            this.vote = vote;
        }

        @Override
        public void run() {
            calculateResults();
        }

        /**
         * Calculate results.
         */
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
                        int kickedout = 1;

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

                        if (vote.getBallots().isEmpty()) {
                            for (Choice c : vote.getChoices()) {
                                resultList.add(new VoteResult(c, 0, vote));
                            }
                        } else {
                            // IRV = 1 winner.
                            if (vote.getMaxChoices() == 1) {
                                int minval;
                                boolean endmenow = false;
                                Map<Choice, Integer> voteCounting = new HashMap<>();

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
                                            for (Choice oc : vote.getChoices())
                                                if (resultList.stream().noneMatch(vr -> vr.getChoice().equals(oc)))
                                                    resultList.add(new VoteResult(oc, 0, vote));
                                            endmenow = true;
                                            break;
                                        }
                                    }
                                    if (endmenow)
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
                                        // We also add it to the list and give the number at which it was kicked out
                                        resultList.add(new VoteResult(minchoice, -kickedout, vote));
                                        kickedout++;
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
                                        if (countMap.get(c) != null &&
                                                countMap.get(c) >= ((double) vote.getBallots().size()) / vote.getMaxChoices()) {
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
                                            if (countMap.get(c) != null && countMap.get(c) != 0 && countMap.get(c) < minval) {
                                                minval = countMap.get(c);
                                                minchoice = c;
                                            }
                                        }

                                        // We register the order in which we kicked it out of the running
                                        resultList.add(new VoteResult(minchoice, -kickedout, vote));
                                        kickedout++;

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

                                for (Choice oc : vote.getChoices())
                                    if (resultList.stream().noneMatch(vr -> vr.getChoice().equals(oc)))
                                        resultList.add(new VoteResult(oc, 0, vote));
                            }
                        }
                        vote.setResultList(resultList);
                        break;
                    default:
                        vote.setResultList(null);
                        break;
                }
//                EntityManager tempEM = Controllers.getEMF().createEntityManager();
//                VoteDAO.of(tempEM).update(vote);
//                tempEM.close();

                VoteDAO.of(Controllers.getEntityManager()).update(vote);
                vote.setPendingResult(false);
            }
        }

        private void redistribute(List<List<Choice>> collated, Choice choice, Map<Choice, Double> countMap, double ratio) {
            for (List<Choice> lc : collated) {
                if (lc.get(0).equals(choice)) {
                    lc.remove(0);
                    countMap.put(lc.get(0), countMap.get(lc.get(0)) + ratio);
                } else
                    lc.remove(choice);
            }
            countMap.remove(choice);
        }
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Controllers.init();
        Vote problem = VoteDAO.of(Controllers.getEntityManager()).findById(42);
        VoteResults vr = new VoteResults(problem);

        vr.calculateResults();
    }
}
