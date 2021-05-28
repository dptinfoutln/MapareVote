package fr.univtln.mapare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class VotedVoteTest {
    @Test
    void testConstructor() {
        Vote vote = new Vote();
        User user = new User();
        VotedVote actualVotedVote = new VotedVote(vote, user);
        assertSame(vote, actualVotedVote.getVote());
        assertSame(user, actualVotedVote.getUser());
    }

    @Test
    void testToString() {
        VotedVote votedVote = new VotedVote();
        votedVote.setUser(new User());
        LocalDate startDate = LocalDate.ofEpochDay(1L);
        LocalDate endDate = LocalDate.ofEpochDay(1L);
        votedVote.setVote(new Vote("Label", startDate, endDate, "Algo", true, new User()));
        assertEquals("VotedVote{token='null', vote=0, user=0}", votedVote.toString());
    }
}

