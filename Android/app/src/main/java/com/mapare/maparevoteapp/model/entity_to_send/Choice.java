package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {

    @JsonProperty("id")
    private final int id;

    @JsonProperty("weight")
    private final int weight;

    public Choice(int id, int weight) {
        this.id = id;
        this.weight = weight;
    }
}
