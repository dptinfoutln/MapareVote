package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = "id")

@Entity
@Table(name = "\"USERS\"")
@NamedQueries({
        @NamedQuery(name = "User.findById", query = "SELECT U FROM User U WHERE U.id = :id"),
        @NamedQuery(name = "User.findByName", query = "SELECT U FROM User U WHERE U.lastname = :lastname"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT U FROM User U WHERE U.email = :email"),
        @NamedQuery(name = "User.findAll", query = "SELECT U FROM User U")
})
public class User implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String firstname;

    @Column(name = "\"emailToken\"")
    private String emailToken;

    @Column(nullable = false)
    private Boolean confirmed;

    @Column(nullable = false)
    private Boolean admin;

    @Column(nullable = false)
    private Boolean banned;

    @OneToMany
    @JoinTable(name = "\"STARTED_VOTES\"",
            joinColumns = @JoinColumn(name = "votemaker"),
            inverseJoinColumns = @JoinColumn(name = "vote"))
    private List<Vote> startedVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    @JoinTable(name = "\"PRIVATE_VOTES\"",
            joinColumns = @JoinColumn(name = "\"user\""),
            inverseJoinColumns = @JoinColumn(name = "\"vote\""))
    private List<Vote> privateVoteList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<VotedVote> votedVotes = new ArrayList<>();

    public User() {
    }

    public User(String email, String lastname, String firstname, String emailToken, Boolean confirmed, Boolean admin, Boolean banned, Boolean deleted) {
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.emailToken = emailToken;
        this.confirmed = confirmed;
        this.admin = admin;
        this.banned = banned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(String confirmationHash) {
        this.emailToken = confirmationHash;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public List<Vote> getStartedVotes() {
        return startedVotes;
    }

    public void setStartedVotes(List<Vote> startedVotes) {
        this.startedVotes = startedVotes;
    }

    public void addStartedVote(Vote vote) {
        if (!startedVotes.contains(vote))
            startedVotes.add(vote);
    }

    public List<VotedVote> getVotedVotes() {
        return votedVotes;
    }

    public void setVotedVotes(List<VotedVote> votedVotes) {
        this.votedVotes = votedVotes;
    }

    public List<Vote> getPrivateVoteList() {
        return privateVoteList;
    }

    public void setPrivateVoteList(List<Vote> privateVoteList) {
        this.privateVoteList = privateVoteList;
    }

    public void addPrivateVote(Vote vote) {
        if (!privateVoteList.contains(vote))
            privateVoteList.add(vote);
    }
}
