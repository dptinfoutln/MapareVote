package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;

/**
 * The type Voted vote.
 */
@JsonIdentityInfo(scope=User.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="token")
public class VotedVote implements Serializable {

    @JsonProperty("token")
    private String token;

    @JsonProperty("vote")
    private Vote vote;

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets vote.
     *
     * @return the vote
     */
    public Vote getVote() {
        return vote;
    }

    /**
     * Sets vote.
     *
     * @param vote the vote
     */
    public void setVote(Vote vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "VotedVote{" +
                "token='" + token + '\'' +
                ", voteId=" + vote.getId() +
                '}';
    }
}
