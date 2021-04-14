package fr.univtln.mapare.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private String email;

    private String lastname;

    private String firstname;

    private String emailToken;

    private Boolean confirmed;

    private Boolean admin;

    private Boolean banned;

    private Boolean deleted;

    @OneToMany
    @JoinTable(name = "STARTED_VOTES",
            joinColumns = @JoinColumn(name = "votemaker"),
            inverseJoinColumns = @JoinColumn(name = "vote"))
    private List<Vote> startedVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "members", cascade = {CascadeType.ALL})
    private List<PrivateVote> privateVoteList = new ArrayList<>();

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
        this.deleted = deleted;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public List<PrivateVote> getPrivateVoteList() {
        return privateVoteList;
    }

    public void setPrivateVoteList(List<PrivateVote> privateVoteList) {
        this.privateVoteList = privateVoteList;
    }

    public void addPrivateVote(PrivateVote vote) {
        if (!privateVoteList.contains(vote))
            privateVoteList.add(vote);
    }
}
