package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"vote", "user"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="token")
@Table(name = "\"VOTED_VOTES\"")
@NamedQueries({
        @NamedQuery(name = "VotedVotes.findByUser", query = "SELECT V FROM VotedVote V WHERE V.user = :user"),
        @NamedQuery(name = "VotedVotes.findByVote", query = "SELECT V FROM VotedVote V WHERE V.vote = :vote")
})
public class VotedVote implements Serializable {

    @Id
    private String token;

    @ManyToOne
    @JoinColumn(nullable = false, name = "\"vote\"")
    private Vote vote;

    @ManyToOne
    @JoinColumn(nullable = false, name = "\"user\"")
    private User user;

    @Builder
    @SneakyThrows
    public VotedVote(Vote vote, User user) {
        //this.token = ;
        this.vote = vote;
        this.user = user;
    }
}

