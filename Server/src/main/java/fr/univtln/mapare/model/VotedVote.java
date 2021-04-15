package fr.univtln.mapare.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "\"VOTED_VOTES\"")
public class VotedVote implements Serializable {

    @Id
    private String token;

    @ManyToOne
    @JoinColumn(name = "\"vote\"")
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "\"user\"")
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

