package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BallotChoice {

    @JsonProperty("choice")
    private final Choice choice;

    @JsonProperty("weight")
    private final int weight;

    public BallotChoice(Choice choice, int weight) {
        this.choice = choice;
        this.weight = weight;
    }
}
