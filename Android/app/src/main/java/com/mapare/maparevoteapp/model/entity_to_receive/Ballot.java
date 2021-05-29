package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mapare.maparevoteapp.model.EntityWithId;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;


/**
 * The type Ballot.
 */
@JsonIdentityInfo(scope= Ballot.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Ballot implements Serializable, EntityWithId {

    @JsonProperty("id")
    private int id;

    @JsonProperty("date")
    private List<String> date;

    @JsonProperty("choices")
    private List<BallotChoice> choices;

    @JsonProperty("vote")
    private Vote vote;

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
     * Gets date.
     *
     * @return the date
     */
    public List<String> getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(List<String> date) {
        this.date = date;
    }

    /**
     * Gets choices.
     *
     * @return the choices
     */
    public List<BallotChoice> getChoices() {
        return choices;
    }

    /**
     * Sets choices.
     *
     * @param choices the choices
     */
    public void setChoices(List<BallotChoice> choices) {
        this.choices = choices;
    }

    @NotNull
    @Override
    public String toString() {
        return "Ballot{" +
                "id=" + id +
                ", date=" + date +
                ", choices=" + choices +
                ", voteId=" + vote.getId() +
                '}';
    }
}
