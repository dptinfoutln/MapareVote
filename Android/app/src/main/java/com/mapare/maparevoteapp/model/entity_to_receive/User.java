package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mapare.maparevoteapp.model.EntityWithId;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;


/**
 * The type User.
 */
@JsonIdentityInfo(scope=User.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User implements Serializable, EntityWithId {

    @JsonProperty("id")
    private int id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("lastname")
    private String name;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("confirmed")
    private boolean confirmed;

    @JsonProperty("banned")
    private boolean banned;

    @JsonProperty("votedVotes")
    private List<VotedVote> votedVotes;

    @JsonProperty("startedVotes")
    private List<Vote> startedVotes;

    @JsonProperty("privateVoteList")
    private List<Vote> privateVoteList;

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets firstname.
     *
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets firstname.
     *
     * @param firstname the firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Is confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Sets confirmed.
     *
     * @param confirmed the confirmed
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * Is banned boolean.
     *
     * @return the boolean
     */
    public boolean isBanned() {
        return banned;
    }

    /**
     * Sets banned.
     *
     * @param banned the banned
     */
    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    /**
     * Gets voted votes.
     *
     * @return the voted votes
     */
    public List<VotedVote> getVotedVotes() {
        return votedVotes;
    }

    /**
     * Sets voted votes.
     *
     * @param votedVotes the voted votes
     */
    public void setVotedVotes(List<VotedVote> votedVotes) {
        this.votedVotes = votedVotes;
    }

    /**
     * Gets started votes.
     *
     * @return the started votes
     */
    public List<Vote> getStartedVotes() {
        return startedVotes;
    }

    /**
     * Sets started votes.
     *
     * @param startedVotes the started votes
     */
    public void setStartedVotes(List<Vote> startedVotes) {
        this.startedVotes = startedVotes;
    }

    /**
     * Gets private vote list.
     *
     * @return the private vote list
     */
    public List<Vote> getPrivateVoteList() {
        return privateVoteList;
    }

    /**
     * Sets private vote list.
     *
     * @param privateVoteList the private vote list
     */
    public void setPrivateVoteList(List<Vote> privateVoteList) {
        this.privateVoteList = privateVoteList;
    }

    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", confirmed=" + confirmed +
                ", banned=" + banned +
                '}';
    }
}
