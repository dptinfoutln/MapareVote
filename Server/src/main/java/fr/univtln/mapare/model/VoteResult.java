package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "\"VOTE_RESULT\"")
@NamedQueries({
        @NamedQuery(name = "VoteResult.findByVote", query = "SELECT V FROM VoteResult V WHERE V.vote = :vote"),
        @NamedQuery(name = "VoteResult.findAll", query = "SELECT V FROM VoteResult V")
})
public class VoteResult implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"vote"})
    @JoinColumn(name = "\"choice\"")
    private Choice choice;

    @Column(name = "\"value\"")
    private int value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "\"vote\"")
    private Vote vote;

    @Override
    public String toString() {
        return "VoteResult{" +
                "choice=" + choice.getNames() +
                ", value=" + value +
                ", vote=" + vote.getLabel() +
                '}';
    }
}
