package fr.univtln.mapare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class VotedVote implements Serializable {

    @Id
    private String token;

    @ManyToOne
    private Vote vote;

    @ManyToOne
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
