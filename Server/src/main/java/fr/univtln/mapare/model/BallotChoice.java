package fr.univtln.mapare.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "\"BALLOT_CHOICE\"")
@NamedQueries({
        @NamedQuery(name = "Choices.findByBallot", query = "SELECT B FROM BallotChoice B WHERE B.ballot = :ballot")
})
public class BallotChoice implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "\"ballot\"")
    private Ballot ballot;

    @Id
    @ManyToOne
    @JoinColumn(name = "\"choice\"")
    private Choice choice;

    private int weight;

    public BallotChoice() {
    }

    public BallotChoice(Ballot ballot, Choice choice) {
        this.ballot = ballot;
        this.choice = choice;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
