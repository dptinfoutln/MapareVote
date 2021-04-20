package fr.univtln.mapare.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "\"VOTE_RESULT\"")
public class VoteResult implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "\"choice\"")
    private Choice choice;

    @Column(name = "\"value\"")
    private int value;

    @ManyToOne
    @JoinColumn(name = "\"vote\"")
    private Vote vote;

}
