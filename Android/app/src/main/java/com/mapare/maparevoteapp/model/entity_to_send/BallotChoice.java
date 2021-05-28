package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

/**
 * The type Ballot choice.
 */
public class BallotChoice {

    @JsonProperty("choice")
    private final Choice choice;

    @JsonProperty("weight")
    private final int weight;

    /**
     * Instantiates a new Ballot choice.
     *
     * @param choice the choice
     * @param weight the weight
     */
    public BallotChoice(Choice choice, int weight) {
        this.choice = choice;
        this.weight = weight;
    }

    @NotNull
    @Override
    public String toString() {
        return "BallotChoice{" +
                "choice=" + choice.getId() +
                ", weight=" + weight +
                '}';
    }
}
