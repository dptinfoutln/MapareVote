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
    @JoinColumn(name = "\"choice\"", nullable = false)
    private Choice choice;

    @ManyToOne
    @JoinColumn(name = "\"vote\"", nullable = false)
    private Vote vote;

    @Column(name = "\"result\"")
    private int result;

    @Override
    public String toString() {
        return "VoteResult{" +
                "choice=" + choice +
                ", result=" + result +
                '}';
    }
}
