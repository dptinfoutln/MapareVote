package fr.univtln.mapare.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"CHOICE\"")
public class Choice implements Serializable {
    @Id
    @GeneratedValue
    int id;

    @ElementCollection
    @CollectionTable(name = "\"CHOICE_DETAILS\"",
            joinColumns = @JoinColumn(name = "id"))
    @Column(name = "choice")
    //@OrderColumn(name="order") not working
    private List<String> names = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "\"vote\"")
    private Vote vote;

    public Choice() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
