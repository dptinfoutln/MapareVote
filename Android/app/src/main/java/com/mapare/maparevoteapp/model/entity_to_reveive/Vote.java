package com.mapare.maparevoteapp.model.entity_to_reveive;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;

@JsonIdentityInfo(scope=Vote.class, generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Vote {

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

    @JsonProperty("resultList")
    private List<VoteResult> resultList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getStartDate() {
        return startDate;
    }

    public void setStartDate(List<String> startDate) {
        this.startDate = startDate;
    }

    public List<String> getEndDate() {
        return endDate;
    }

    public void setEndDate(List<String> endDate) {
        this.endDate = endDate;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isIntermediaryResult() {
        return intermediaryResult;
    }

    public void setIntermediaryResult(boolean intermediaryResult) {
        this.intermediaryResult = intermediaryResult;
    }

    public User getVotemaker() {
        return votemaker;
    }

    public void setVotemaker(User votemaker) {
        this.votemaker = votemaker;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public int getMaxChoices() {
        return maxChoices;
    }

    public void setMaxChoices(int maxChoices) {
        this.maxChoices = maxChoices;
    }

    public List<VoteResult> getResultList() {
        return resultList;
    }

    public void setResultList(List<VoteResult> resultList) {
        this.resultList = resultList;
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
                ", resultList=" + resultList +
                '}';
    }
}
