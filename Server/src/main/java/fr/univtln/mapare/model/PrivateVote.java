package fr.univtln.mapare.model;

import java.util.ArrayList;
import java.util.List;

public class PrivateVote extends Vote {
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
