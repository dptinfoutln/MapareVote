package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * The type Ballot choice.
 */
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

    /**
     * Gets ballot.
     *
     * @return the ballot
     */
    public Ballot getBallot() {
        return ballot;
    }

    /**
     * Sets ballot.
     *
     * @param ballot the ballot
     */
    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

    /**
     * Gets choice.
     *
     * @return the choice
     */
    public Choice getChoice() {
        return choice;
    }

    /**
     * Sets choice.
     *
     * @param choice the choice
     */
    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets weight.
     *
     * @param weight the weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @NotNull
    @Override
    public String toString() {
        return "BallotChoice{" +
                ", choice=" + choice +
                ", weight=" + weight +
                '}';
    }
}
