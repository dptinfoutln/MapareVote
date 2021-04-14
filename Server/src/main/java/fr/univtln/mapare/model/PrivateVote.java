package fr.univtln.mapare.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PrivateVote extends Vote{


    @ManyToMany
    @JoinTable(name = "\"PRIVATE_VOTES\"",
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<User> members = new ArrayList<>();

    public PrivateVote() {
        super();
    }

    public PrivateVote(Vote vote) {
        super(vote.getId(), vote.getLabel(), vote.getStartDate(), vote.getEndDate(),
                vote.getAlgo(), vote.getAnonymous(), vote.getVotemaker());
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        if (!members.contains(user))
            members.add(user);
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
