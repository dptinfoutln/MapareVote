package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;

/**
 * The type Vote result.
 */
@JsonIdentityInfo(scope=User.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="choice")
public class VoteResult implements Serializable {

    @JsonProperty("choice")
    private Choice choice;

    @JsonProperty("value")
    private int value;

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
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "VoteResult{" +
                "choiceId=" + choice.getId() +
                ", value=" + value +
                '}';
    }
}
