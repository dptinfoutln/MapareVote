package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Choice {

    @JsonProperty("id")
    private final int id;

    public Choice(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
