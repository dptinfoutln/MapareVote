package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = "id")

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"CHOICE\"")
@NamedQueries({
        @NamedQuery(name = "Choice.findByVote", query = "SELECT C FROM Choice C WHERE C.vote = :vote")
})
public class Choice implements Serializable {
    @Id
    @GeneratedValue
    int id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "\"CHOICE_DETAILS\"",
            joinColumns = @JoinColumn(name = "id"))
    @OrderColumn(name="\"order\"")
    @Column(nullable = false, name = "\"choice\"")
    private List<String> names = new ArrayList<>();


    @ManyToOne
    @JoinColumn(nullable = false, name = "\"vote\"")
    private Vote vote;

    public Choice() {
    }

    public Choice(List<String> names, Vote vote) {
        this.names = names;
        this.vote = vote;
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
