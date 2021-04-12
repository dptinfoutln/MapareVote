package fr.univtln.mapare.Model;

import java.util.List;

public class PrivateVote extends Vote {
    private List<User> members;

    public PrivateVote() {
        super();
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
