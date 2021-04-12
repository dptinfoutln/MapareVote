package fr.univtln.mapare.Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Ballot {
    private String identifyingHash;
    private LocalDateTime date;
    private Vote vote;
    private Optional<User> voter;
    private Choice choices;

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

    public Choice getChoices() {
        return choices;
    }

    public void setChoices(Choice choices) {
        this.choices = choices;
    }
}
