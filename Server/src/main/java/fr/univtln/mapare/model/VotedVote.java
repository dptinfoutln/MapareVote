package fr.univtln.mapare.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(of = "token")

@Entity
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

    public VotedVote() {
    }

    public VotedVote(String token, Vote vote, User user) {
        this.token = token;
        this.vote = vote;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

