package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VOTE")
public class Vote implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private String label;
    private LocalDate startDate;
    private LocalDate endDate;
    private String algo; //TODO: find better name
    private Boolean anonymous;

    @OneToOne
    private User votemaker;

    @OneToMany(mappedBy = "vote", cascade = {CascadeType.ALL})
    private List<Ballot> ballots = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = {CascadeType.ALL})
    private List<Choice> choices = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = {CascadeType.ALL})
    private List<VotedVote> votedVotes = new ArrayList<>();

    public Vote() {
    }

    public Vote(int id, String label, LocalDate startDate, LocalDate endDate, String algo, Boolean anonymous, User votemaker) {
        this.id = id;
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.algo = algo;
        this.anonymous = anonymous;
        this.votemaker = votemaker;
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
        return votemaker;
    }

    public void setVotemaker(User votemaker) {
        this.votemaker = votemaker;
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

    public List<VotedVote> getVotedVotes() {
        return votedVotes;
    }

    public void setVotedVotes(List<VotedVote> votedVotes) {
        this.votedVotes = votedVotes;
    }
}
