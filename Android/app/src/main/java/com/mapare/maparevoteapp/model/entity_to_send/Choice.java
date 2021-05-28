package com.mapare.maparevoteapp.model.entity_to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Choice.
 */
public class Choice {

    @JsonProperty("id")
    private final int id;

    /**
     * Instantiates a new Choice.
     *
     * @param id the id
     */
    public Choice(int id) {
        this.id = id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }
}
