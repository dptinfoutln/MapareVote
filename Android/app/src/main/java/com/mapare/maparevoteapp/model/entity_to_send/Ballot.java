package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The type Ballot.
 */
public class Ballot {

    @JsonProperty("choices")
    private final List<BallotChoice> choices;

    /**
     * Instantiates a new Ballot.
     *
     * @param choices the choices
     */
    public Ballot(List<BallotChoice> choices) {
        this.choices = choices;
    }

    @NotNull
    @Override
    public String toString() {
        return "Ballot{" +
                "choices=" + choices +
                '}';
    }
}
