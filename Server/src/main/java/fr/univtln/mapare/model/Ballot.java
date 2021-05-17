package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Ballot.
 */
@Data
@EqualsAndHashCode(of = {"vote", "voter"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "\"BALLOT\"")
@NamedQueries({
        @NamedQuery(name = "Ballot.findByVoter", query = "SELECT B FROM Ballot B WHERE B.voter = :voter"),
        @NamedQuery(name = "Ballot.findByVote", query = "SELECT B FROM Ballot B WHERE B.vote = :vote"),
        @NamedQuery(name = "Ballot.findAll", query = "SELECT B FROM Ballot B"),
        @NamedQuery(name = "Ballot.findByVoteByVoter", query = "SELECT B FROM Ballot B WHERE B.vote = :vote AND B.voter = :voter")
})
public class Ballot implements Serializable {

    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JsonIgnoreProperties({"votemaker", "members", "choices", "resultList", "votedVotes", "ballots"})
    @JoinColumn(nullable = false, name = "\"vote\"")
    private Vote vote;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "\"voter\"", nullable = true)
    private User voter;

    @OneToMany(mappedBy = "ballot", cascade = CascadeType.ALL)
    private List<BallotChoice> choices = new ArrayList<>();

    /**
     * Instantiates a new Ballot.
     *
     * @param date  the date
     * @param vote  the vote
     * @param voter the voter
     */
    @Builder
    @SneakyThrows
    public Ballot(LocalDateTime date, Vote vote, User voter) {
        this.date = date;
        this.vote = vote;
        this.voter = voter;
    }

    /**
     * Add choice.
     *
     * @param choice the choice
     */
    public void addChoice(BallotChoice choice) {
        if (!choices.contains(choice))
            choices.add(choice);
    }

    @Override
    public String toString() {
        return "Ballot{" +
                "id=" + id +
                ", date=" + date +
                ", vote=" + vote.getId() +
                ", voter=" + voter.getId() +
                ", choices=" + choices +
                '}';
    }
}
