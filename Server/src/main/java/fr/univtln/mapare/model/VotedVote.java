package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * The type Voted vote.
 */
@Data
@EqualsAndHashCode(of = {"vote", "user"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "token")
@Table(name = "\"VOTED_VOTES\"")//, uniqueConstraints = @UniqueConstraint(columnNames={"vote", "user"}))
@NamedQueries({
        @NamedQuery(name = "VotedVotes.findByToken", query = "SELECT V FROM VotedVote V WHERE V.token = :token"),
        @NamedQuery(name = "VotedVotes.findByVote", query = "SELECT V FROM VotedVote V WHERE V.vote = :vote"),
        @NamedQuery(name = "VotedVotes.findByUser", query = "SELECT V FROM VotedVote V WHERE V.user = :user"),
        @NamedQuery(name = "VotedVotes.findByUser&Vote", query = "SELECT V FROM VotedVote V WHERE V.user = :user AND V.vote = :vote"),
        @NamedQuery(name = "VotedVotes.findAll", query = "SELECT V FROM VotedVote V")
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

    /**
     * Instantiates a new Voted vote.
     *
     * @param vote the vote
     * @param user the user
     */
    @Builder
    @SneakyThrows
    public VotedVote(Vote vote, User user) {
        this.vote = vote;
        this.user = user;
        SecureRandom secureRandom = new SecureRandom(); //threadsafe
        Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        this.token = base64Encoder.encodeToString(randomBytes);
    }

    @Override
    public String toString() {
        return "VotedVote{" +
                "token='" + token + '\'' +
                ", vote=" + vote.getId() +
                ", user=" + user.getId() +
                '}';
    }
}

