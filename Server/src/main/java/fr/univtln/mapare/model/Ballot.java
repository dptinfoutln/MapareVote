package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = "id")

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"BALLOT\"")
@NamedQueries({
        @NamedQuery(name = "Ballot.findByVoter", query = "SELECT B FROM Ballot B WHERE B.voter = :voter"),
        @NamedQuery(name = "Ballot.findByVote", query = "SELECT B FROM Ballot B WHERE B.vote = :vote"),
})
public class Ballot implements Serializable {

    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(nullable = false, name = "\"vote\"")
    private Vote vote;

    @OneToOne
    @JoinColumn(name = "\"voter\"", nullable = true)
    private User voter;

    @OneToMany(mappedBy = "ballot")
    private List<BallotChoice> choices = new ArrayList<>();

    public Ballot() {
    }

    public Ballot(LocalDateTime date, Vote vote) {
        this.id = id;
        this.date = date;
        this.vote = vote;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
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
