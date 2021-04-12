package fr.univtln.mapare.Model;

import java.util.List;

public class User {
    private String email;
    private String nom;
    private String prenom;
    private String confirmationHash;
    private Boolean confirmed;
    private Boolean admin;
    private Boolean banned;
    private List<Vote> startedVotes;
    private List<Vote> votedVotes;
    private List<PrivateVote> privateVoteList;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getConfirmationHash() {
        return confirmationHash;
    }

    public void setConfirmationHash(String confirmationHash) {
        this.confirmationHash = confirmationHash;
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

    public List<Vote> getVotedVotes() {
        return votedVotes;
    }

    public void setVotedVotes(List<Vote> votedVotes) {
        this.votedVotes = votedVotes;
    }

    public void addVotedVote(Vote vote) {
        if (!votedVotes.contains(vote))
            votedVotes.add(vote);
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
