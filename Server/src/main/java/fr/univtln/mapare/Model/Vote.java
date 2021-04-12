package fr.univtln.mapare.Model;

import java.time.LocalDate;
import java.util.List;

public class Vote {
    private int id;
    private String label;
    private LocalDate startDate;
    private LocalDate endDate;
    private String algo; //TODO: find better name
    private Boolean anonymous;
    private User Votemaker;
    private List<Ballot> ballots;
    private List<User> voters;
    private List<Choice> choices;

    public Vote() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public User getVotemaker() {
        return Votemaker;
    }

    public void setVotemaker(User votemaker) {
        Votemaker = votemaker;
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    public void addBallot(Ballot ballot) {
        if (!ballots.contains(ballot))
            ballots.add(ballot);
    }

    public List<User> getVoters() {
        return voters;
    }

    public void setVoters(List<User> voters) {
        this.voters = voters;
    }

    public void addVoter(User voter) {
        if (!voters.contains(voter))
            voters.add(voter);
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addChoice(Choice choice) {
        if (!choices.contains(choice))
            choices.add(choice);
    }
}
