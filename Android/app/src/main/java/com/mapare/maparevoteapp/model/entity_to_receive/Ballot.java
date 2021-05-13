package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.List;


@JsonIdentityInfo(scope= Ballot.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Ballot implements Serializable {

    @JsonProperty("id")
    private int id;

    @JsonProperty("date")
    private List<String> date;

    @JsonProperty("choices")
    private List<BallotChoice> choices;

    @JsonProperty("vote")
    private Vote vote;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<BallotChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<BallotChoice> choices) {
        this.choices = choices;
    }

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
