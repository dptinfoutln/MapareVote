package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(of = {"id", "vote", "names"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"CHOICE\"")
@NamedQueries({
        @NamedQuery(name = "Choice.findAll", query = "SELECT C FROM Choice C"),
        @NamedQuery(name = "Choice.findByVote", query = "SELECT C FROM Choice C WHERE C.vote = :vote")
})
public class Choice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    @SneakyThrows
    public Choice(List<String> names, Vote vote) {
        this.names = names;
        this.vote = vote;
    }

    public void addName(String name) {
        if (!names.contains(name))
            names.add(name);
    }

    @Override
    public String toString() {
        return "Choice{" +
                "id=" + id +
                ", names=" + names +
                ", vote=" + vote.getId() +
                '}';
    }
}
