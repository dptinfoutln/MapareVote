package com.mapare.maparevoteapp.model.entity_to_receive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mapare.maparevoteapp.model.EntityWithId;

import java.io.Serializable;
import java.util.List;

/**
 * The type Vote.
 */
@JsonIdentityInfo(scope=Vote.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Vote implements Serializable, EntityWithId {

    @JsonProperty("id")
    private int id;

    @JsonProperty("label")
    private String label;

    @JsonProperty("startDate")
    private List<String> startDate;

    @JsonProperty("endDate")
    private List<String> endDate;

    @JsonProperty("algo")
    private String algo;

    @JsonProperty("anonymous")
    private boolean anonymous;

    @JsonProperty("intermediaryResult")
    private boolean intermediaryResult;

    @JsonProperty("votemaker")
    private User votemaker;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("maxChoices")
    private int maxChoices;

    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public List<String> getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(List<String> startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public List<String> getEndDate() {
        return endDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(List<String> endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets algo.
     *
     * @return the algo
     */
    public String getAlgo() {
        return algo;
    }

    /**
     * Sets algo.
     *
     * @param algo the algo
     */
    public void setAlgo(String algo) {
        this.algo = algo;
    }

    /**
     * Is anonymous boolean.
     *
     * @return the boolean
     */
    public boolean isAnonymous() {
        return anonymous;
    }

    /**
     * Sets anonymous.
     *
     * @param anonymous the anonymous
     */
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * Is intermediary result boolean.
     *
     * @return the boolean
     */
    public boolean isIntermediaryResult() {
        return intermediaryResult;
    }

    /**
     * Sets intermediary result.
     *
     * @param intermediaryResult the intermediary result
     */
    public void setIntermediaryResult(boolean intermediaryResult) {
        this.intermediaryResult = intermediaryResult;
    }

    /**
     * Gets votemaker.
     *
     * @return the votemaker
     */
    public User getVotemaker() {
        return votemaker;
    }

    /**
     * Sets votemaker.
     *
     * @param votemaker the votemaker
     */
    public void setVotemaker(User votemaker) {
        this.votemaker = votemaker;
    }

    /**
     * Gets choices.
     *
     * @return the choices
     */
    public List<Choice> getChoices() {
        return choices;
    }

    /**
     * Sets choices.
     *
     * @param choices the choices
     */
    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    /**
     * Gets max choices.
     *
     * @return the max choices
     */
    public int getMaxChoices() {
        return maxChoices;
    }

    /**
     * Sets max choices.
     *
     * @param maxChoices the max choices
     */
    public void setMaxChoices(int maxChoices) {
        this.maxChoices = maxChoices;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", algo='" + algo + '\'' +
                ", anonymous=" + anonymous +
                ", intermediaryResult=" + intermediaryResult +
                ", votemaker=" + votemaker +
                ", choices=" + choices +
                ", maxChoices=" + maxChoices +
                '}';
    }
}
