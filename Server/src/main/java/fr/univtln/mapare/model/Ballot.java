package fr.univtln.mapare.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Ballot {
    private String identifyingHash;
    private LocalDateTime date;
    private Vote vote;
    private Optional<User> voter;
    private List<Choice> choices = new ArrayList<>();

    public Ballot() {
    }

    public String getIdentifyingHash() {
        return identifyingHash;
    }

    public void setIdentifyingHash(String identifyingHash) {
        this.identifyingHash = identifyingHash;
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

    public List<Choice> getChoices() {
        return choices;
    }

    public void addChoice(Choice choice) {
        if (!choices.contains(choice))
            choices.add(choice);
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
