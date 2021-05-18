package fr.univtln.mapare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class VoteResultTest {
    @Test
    public void testToString() {
        VoteResult voteResult = new VoteResult();
        voteResult.setVote(new Vote());
        ArrayList<String> names = new ArrayList<String>();
        voteResult.setChoice(new Choice(names, new Vote()));
        assertEquals("VoteResult{choice=[], value=0, vote=null}", voteResult.toString());
    }
}

