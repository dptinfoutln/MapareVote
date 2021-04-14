package fr.univtln.mapare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRIVATE_VOTES")
public class PrivateVote extends Vote implements Serializable {
    @ManyToMany(mappedBy = " privateVoteList")
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
