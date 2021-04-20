package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.persistence.annotations.DiscriminatorClass;
import org.eclipse.persistence.annotations.PrivateOwned;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"label", "votemaker"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"VOTE\"")
@NamedQueries({
        @NamedQuery(name = "Vote.findByVotemaker", query = "SELECT V FROM Vote V WHERE V.votemaker = :votemaker"),
        @NamedQuery(name = "Vote.findPublic", query = "SELECT V FROM Vote V WHERE V.members IS EMPTY AND V.deleted = false"),
        @NamedQuery(name = "Vote.findPrivateByUser", query = "SELECT V FROM Vote V WHERE :user MEMBER OF V.members AND V.deleted = false"),
        @NamedQuery(name = "Vote.findAll", query = "SELECT V FROM Vote V")
})
public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false, name = "\"startDate\"")
    private LocalDate startDate;

    @Column(name = "\"endDate\"")
    private LocalDate endDate;

    @Column(nullable = false)
    private String algo; //TODO: find better name

    @Transient
    private Boolean _private;

    @Column(nullable = false)
    private Boolean anonymous;

    @Column(nullable = false)
    private Boolean deleted = false;

    @ManyToOne
    @JoinTable(name = "\"STARTED_VOTES\"",
            joinColumns = @JoinColumn(name = "\"vote\""),
            inverseJoinColumns = @JoinColumn(name = "\"votemaker\""))
    @JsonIgnoreProperties({"startedVotes", "privateVoteList", "votedVotes", "emailToken"})
    private User votemaker;

    @JsonIgnore
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<Ballot> ballots = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "vote", cascade = CascadeType.ALL)
    @PrivateOwned   // Permert d'update la bd à partir de la liste actuelle (pour les remove par ex)
    private List<Choice> choices = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VotedVote> votedVotes = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name= "\"PRIVATE_VOTES\"",
            joinColumns = @JoinColumn(name = "\"vote\""),
            inverseJoinColumns = @JoinColumn(name = "\"user\""))
    @JsonIgnoreProperties({"startedVotes", "privateVoteList", "votedVotes"})
    private List<User> members = new ArrayList<>();

    @OneToMany(mappedBy = "vote")
    @JoinColumn(name = "\"result\"")
    private List<VoteResult> resultList;

    @Builder
    @SneakyThrows
    public Vote(String label, LocalDate startDate, LocalDate endDate, String algo, Boolean anonymous, User votemaker) {
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.algo = algo;
        this.anonymous = anonymous;
        this.votemaker = votemaker;
    }

    public void addBallot(Ballot ballot) {
        if (!ballots.contains(ballot))
            ballots.add(ballot);
    }

    public void addChoice(Choice choice) {
        if (!choices.contains(choice))
            choices.add(choice);
    }

    public void addMember(User member) {
        if (!members.contains(member))
            members.add(member);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", algo='" + algo + '\'' +
                ", _private=" + _private +
                ", anonymous=" + anonymous +
                ", deleted=" + deleted +
                ", votemaker=" + votemaker +
                ", choices=" + choices +
                ", resultList=" + resultList +
                '}';
    }
}
