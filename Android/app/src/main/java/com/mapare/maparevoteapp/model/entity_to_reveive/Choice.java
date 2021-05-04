package com.mapare.maparevoteapp.model.entity_to_reveive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

@JsonIdentityInfo(scope=Choice.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Choice {

    @JsonProperty("id")
    private int id;

    @JsonProperty("names")
    private List<String> names;

    @JsonProperty("vote")
    private Vote vote;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "id=" + id +
                ", names=" + names +
                ", voteId=" + vote.getId() +
                '}';
    }
}
