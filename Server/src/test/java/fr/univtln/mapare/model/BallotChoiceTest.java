package fr.univtln.mapare.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BallotChoiceTest {
    @Test
    void testToString() {
        BallotChoice ballotChoice = new BallotChoice();
        ballotChoice.setBallot(new Ballot());
        assertEquals("BallotChoice{ballot=0, choice=null, weight=0}", ballotChoice.toString());
    }

    @Test
    void testGetId() {
        BallotChoice ballotChoice = new BallotChoice();
        ballotChoice.setChoice(new Choice());
        LocalDateTime date = LocalDateTime.of(1, 1, 1, 1, 1);
        Vote vote = new Vote();
        ballotChoice.setBallot(new Ballot(date, vote, new User()));
        assertEquals("0 0", ballotChoice.getId());
    }
}

