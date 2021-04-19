package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(of = {"vote", "voter"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"BALLOT\"")
@NamedQueries({
        @NamedQuery(name = "Ballot.findByVoter", query = "SELECT B FROM Ballot B WHERE B.voter = :voter"),
        @NamedQuery(name = "Ballot.findByVote", query = "SELECT B FROM Ballot B WHERE B.vote = :vote"),
})
public class Ballot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(nullable = false, name = "\"vote\"")
    private Vote vote;

    @OneToOne
    @JoinColumn(name = "\"voter\"", nullable = true)
    private User voter;

    @OneToMany(mappedBy = "ballot", cascade = CascadeType.ALL)
    private List<BallotChoice> choices = new ArrayList<>();

    @Builder
    @SneakyThrows
    public Ballot(LocalDateTime date, Vote vote, User voter) {
        this.date = date;
        this.vote = vote;
        this.voter = voter;
    }

    public void addChoice(BallotChoice choice) {
        if (!choices.contains(choice))
            choices.add(choice);
    }
}
