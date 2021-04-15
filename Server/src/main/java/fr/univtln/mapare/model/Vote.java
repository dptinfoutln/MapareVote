package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import org.eclipse.persistence.annotations.DiscriminatorClass;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = "id")

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Table(name = "\"VOTE\"")
@NamedQueries({
        @NamedQuery(name = "Vote.findById", query = "SELECT V FROM Vote V WHERE V.id = :id"),
        @NamedQuery(name = "Vote.findByVotemaker", query = "SELECT V FROM Vote V WHERE V.votemaker = :votemaker"),
        @NamedQuery(name = "Vote.findPublic", query = "SELECT V FROM Vote V WHERE V.members IS EMPTY"),
        @NamedQuery(name = "Vote.findPrivateByUser", query = "SELECT V FROM Vote V WHERE :user MEMBER OF V.members"),
})
public class Vote implements Serializable {
    @Id
    @GeneratedValue
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
    private Boolean anonymous;

    @Column(nullable = false)
    private Boolean deleted;

    @OneToOne
    @JoinColumn(nullable = false, name = "\"votemaker\"")
    @JsonIgnoreProperties({"startedVotes", "privateVoteList", "votedVotes"})
    private User votemaker;

    @JsonIgnore
    @OneToMany(mappedBy = "vote", cascade = {CascadeType.ALL})
    private List<Ballot> ballots = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "vote", cascade = {CascadeType.ALL})
    private List<Choice> choices = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "vote", cascade = {CascadeType.ALL})
    private List<VotedVote> votedVotes = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name= "\"PRIVATE_VOTES\"",
            joinColumns = @JoinColumn(name = "\"vote\""),
            inverseJoinColumns = @JoinColumn(name = "\"user\""))
    @JsonIgnoreProperties({"startedVotes", "privateVoteList", "votedVotes"})
    private List<User> members = new ArrayList<>();

    public Vote() {
    }

    public Vote(String label, LocalDate startDate, LocalDate endDate, String algo, Boolean anonymous, Boolean deleted, User votemaker) {
        this.id = id;
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.algo = algo;
        this.anonymous = anonymous;
        this.deleted = deleted;
        this.votemaker = votemaker;
    }

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public User getVotemaker() {
        return votemaker;
    }

    public void setVotemaker(User votemaker) {
        this.votemaker = votemaker;
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    public void addBallot(Ballot ballot) {
        if (!ballots.contains(ballot))
            ballots.add(ballot);
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addChoice(Choice choice) {
        if (!choices.contains(choice))
            choices.add(choice);
    }

    public List<VotedVote> getVotedVotes() {
        return votedVotes;
    }

    public void setVotedVotes(List<VotedVote> votedVotes) {
        this.votedVotes = votedVotes;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
