package fr.univtln.mapare.model;

import java.util.List;

public class Choice {
    List<String> names;
    private Vote vote;

    public Choice() {
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void addName(String name) {
        if (!names.contains(name))
            names.add(name);
    }
}