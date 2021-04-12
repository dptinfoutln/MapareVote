package fr.univtln.mapare.Model;

import java.util.List;

public class MultiChoice extends Choice {
    List<String> names;

    public MultiChoice() {
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

