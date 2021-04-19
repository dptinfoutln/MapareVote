package fr.univtln.mapare.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"BALLOT_CHOICE\"")
@NamedQueries({
        @NamedQuery(name = "BallotChoice.findByBallot", query = "SELECT B FROM BallotChoice B WHERE B.ballot = :ballot"),
        @NamedQuery(name = "BallotChoice.findAll", query = "SELECT B FROM BallotChoice B")
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
}
