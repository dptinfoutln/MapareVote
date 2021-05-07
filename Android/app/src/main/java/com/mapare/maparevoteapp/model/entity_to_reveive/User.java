package com.mapare.maparevoteapp.model.entity_to_reveive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.List;


@JsonIdentityInfo(scope=User.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User implements Serializable {

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public List<VotedVote> getVotedVotes() {
        return votedVotes;
    }

    public void setVotedVotes(List<VotedVote> votedVotes) {
        this.votedVotes = votedVotes;
    }

    public List<Vote> getStartedVotes() {
        return startedVotes;
    }

    public void setStartedVotes(List<Vote> startedVotes) {
        this.startedVotes = startedVotes;
    }

    public List<Vote> getPrivateVoteList() {
        return privateVoteList;
    }

    public void setPrivateVoteList(List<Vote> privateVoteList) {
        this.privateVoteList = privateVoteList;
    }

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
