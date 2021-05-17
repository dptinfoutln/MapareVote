package fr.univtln.mapare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"BALLOT_CHOICE\"")
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

    @Override
    public String toString() {
        return "BallotChoice{" +
                "ballot=" + ballot.getId() +
                ", choice=" + choice +
                ", weight=" + weight +
                '}';
    }

    @Transient
    public String getId() {
        return ballot.getId() + " " + choice.getId();
    }
}
