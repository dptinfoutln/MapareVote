package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.eclipse.persistence.annotations.PrivateOwned;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(of = {"label", "votemaker"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"VOTE\"")
@NamedQueries({
        @NamedQuery(name = "Vote.findByVotemaker", query = "SELECT V FROM Vote V WHERE V.votemaker = :votemaker AND V.deleted = false"),
        @NamedQuery(name = "Vote.findByVotemaker", query = "SELECT V FROM Vote V WHERE V.votemaker = :votemaker AND V.deleted = false"),
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

    @Column(nullable = false)
    private boolean anonymous;

    @JsonIgnore
    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, name = "\"intermediaryResult\"")
    private boolean intermediaryResult = false;

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

    //TODO: new field for number of winners for STV
    @Column(nullable = false, name = "\"maxChoices\"")
    private int maxChoices = 1;

    @JsonIgnore
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VotedVote> votedVotes = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name= "\"PRIVATE_VOTES\"",
            joinColumns = @JoinColumn(name = "\"vote\""),
            inverseJoinColumns = @JoinColumn(name = "\"user\""))
    @JsonIgnoreProperties({"startedVotes", "privateVoteList", "votedVotes", "confirmed", "admin", "banned",
            "passwordHash", "salt", "emailToken"})
    private List<User> members = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    @JoinColumn(name = "\"result\"")
    private List<VoteResult> resultList;

    @JsonIgnore
    private transient LocalDate lastCalculated = null;

    @Transient
    private boolean pendingResult = false;

    @Builder
    @SneakyThrows
    public Vote(String label, LocalDate startDate, LocalDate endDate, String algo, boolean anonymous, User votemaker) {
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

    private static final transient List<String> algolist = Arrays.asList("majority", "borda", "STV");

    public static List<String> getAlgolist() {
        return algolist;
    }

    @JsonIgnore
    public boolean isPublic() {
        return members.isEmpty();
    }

    @JsonIgnore
    public boolean isPrivate() {
        return !isPublic();
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
                ", deleted=" + deleted +
                ", votemaker=" + votemaker +
                ", choices=" + choices +
                ", resultList=" + resultList +
                '}';
    }

    public boolean hasResults() {
        return resultList.isEmpty();
    }
}
