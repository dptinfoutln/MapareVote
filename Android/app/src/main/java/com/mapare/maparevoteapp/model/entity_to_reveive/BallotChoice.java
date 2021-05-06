package com.mapare.maparevoteapp.model.entity_to_reveive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;

@JsonIdentityInfo(scope= BallotChoice.class, generator= ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BallotChoice implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ballot")
    private Ballot ballot;

    @JsonProperty("choice")
    private Choice choice;

    @JsonProperty("weight")
    private int weight;

    public Ballot getBallot() {
        return ballot;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
