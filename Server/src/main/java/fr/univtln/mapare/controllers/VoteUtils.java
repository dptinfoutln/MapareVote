package fr.univtln.mapare.controllers;

import fr.univtln.mapare.model.Vote;

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
            vote.calculateResults();
        }
    }
}
