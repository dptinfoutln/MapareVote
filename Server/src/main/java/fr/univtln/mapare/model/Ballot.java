package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "BALLOT")
public class Ballot implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    int id;


    private LocalDateTime date;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JsonIdentityReference(alwaysAsId = true)
    private Vote vote;

    @OneToOne
    @JoinColumn(nullable = true)
    private User voter;


    private List<Choice> choices = new ArrayList<>();

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
