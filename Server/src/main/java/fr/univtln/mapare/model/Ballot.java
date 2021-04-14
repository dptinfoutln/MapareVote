package fr.univtln.mapare.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Ballot {
    int id;
    private LocalDateTime date;
    private Vote vote;
    private Optional<User> voter;
    private List<BallotChoice> choices = new ArrayList<>();

    public Ballot() {
    }

    public Ballot(int id, LocalDateTime date, Vote vote) {
        this.id = id;
        this.date = date;
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public Optional<User> getVoter() {
        return voter;
    }

    public void setVoter(Optional<User> voter) {
        this.voter = voter;
    }

    public List<BallotChoice> getChoices() {
        return choices;
    }

    public void addChoice(BallotChoice choice) {
        if (!choices.contains(choice))
            choices.add(choice);
    }

    public void setChoices(List<BallotChoice> choices) {
        this.choices = choices;
    }
}
