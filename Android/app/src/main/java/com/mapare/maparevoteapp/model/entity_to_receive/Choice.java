package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mapare.maparevoteapp.model.EntityWithId;

import java.io.Serializable;
import java.util.List;

/**
 * The type Choice.
 */
@JsonIdentityInfo(scope=Choice.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Choice implements Serializable, EntityWithId {

    @JsonProperty("id")
    private int id;

    @JsonProperty("names")
    private List<String> names;

    @JsonProperty("vote")
    private Vote vote;

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
     * Gets names.
     *
     * @return the names
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * Sets names.
     *
     * @param names the names
     */
    public void setNames(List<String> names) {
        this.names = names;
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
        return "Choice{" +
                "id=" + id +
                ", names=" + names +
                '}';
    }
}
