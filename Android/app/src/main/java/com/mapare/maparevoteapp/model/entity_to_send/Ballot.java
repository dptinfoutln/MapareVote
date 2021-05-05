package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Ballot {

    @JsonProperty("choices")
    private final List<Choice> choices;

    public Ballot(List<Choice> choices) {
        this.choices = choices;
    }
}
