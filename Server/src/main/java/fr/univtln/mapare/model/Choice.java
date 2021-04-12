package fr.univtln.mapare.model;

public abstract class Choice {
    private Vote vote;
    private int score;

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
