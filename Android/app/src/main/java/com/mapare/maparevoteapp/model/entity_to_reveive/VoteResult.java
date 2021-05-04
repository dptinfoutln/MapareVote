package com.mapare.maparevoteapp.model.entity_to_reveive;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VoteResult {

    @JsonProperty("choice")
    private Choice choice;

    @JsonProperty("value")
    private int value;

    @JsonProperty("vote")
    private Vote vote;

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "VoteResult{" +
                "choiceId=" + choice.getId() +
                ", value=" + value +
                ", voteId=" + vote.getId() +
                '}';
    }
}
