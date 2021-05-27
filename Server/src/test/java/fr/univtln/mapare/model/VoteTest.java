package fr.univtln.mapare.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VoteTest {
    @Test
    void testConstructor() {
        LocalDate startDate = LocalDate.ofEpochDay(1L);
        LocalDate endDate = LocalDate.ofEpochDay(1L);
        Vote actualVote = new Vote("Label", startDate, endDate, "Algo", true, new User());
        assertEquals("Algo", actualVote.getAlgo());
        assertEquals("Vote{id=0, label='Label', startDate=1970-01-02, endDate=1970-01-02, algo='Algo', anonymous=true,"
                + " deleted=false, votemaker=0, choices=[], resultList=null}", actualVote.toString());
        assertTrue(actualVote.isPublic());
        assertFalse(actualVote.isPendingResult());
        assertFalse(actualVote.isIntermediaryResult());
        assertFalse(actualVote.isDeleted());
        assertTrue(actualVote.isAnonymous());
        assertEquals(1, actualVote.getMaxChoices());
        assertEquals("Label", actualVote.getLabel());
        assertNull(actualVote.getLastCalculated());
    }

    @Test
    void testAddBallot() {
        Vote vote = new Vote();
        vote.addBallot(new Ballot());
        assertEquals(1, vote.getBallots().size());
    }

    @Test
    void testAddBallot2() {
        Vote vote = new Vote();
        vote.addBallot(new Ballot());
        Ballot ballot = new Ballot();
        vote.addBallot(ballot);
        assertEquals(0, ballot.getId());
        assertTrue(vote.isPublic());
        assertFalse(vote.isPendingResult());
        assertFalse(vote.isIntermediaryResult());
        assertFalse(vote.isDeleted());
        assertFalse(vote.isAnonymous());
        assertEquals(1, vote.getMaxChoices());
        assertEquals(0, vote.getId());
    }

    @Test
    void testAddBallot4() {
        Vote vote = new Vote();
        LocalDateTime date = LocalDateTime.of(1, 1, 1, 1, 1);
        Vote vote1 = new Vote();
        Ballot ballot = new Ballot(date, vote1, new User());
        vote.addBallot(ballot);
        LocalDateTime date1 = LocalDateTime.of(1, 1, 1, 1, 1);
        Vote vote2 = new Vote();
        Ballot ballot1 = new Ballot(date1, vote2, new User());
        vote.addBallot(ballot1);
        assertEquals(ballot, ballot1);
        assertEquals("Ballot{id=0, date=0001-01-01T01:01, vote=0, voter=0, choices=[]}", ballot1.toString());
        assertEquals(vote, ballot1.getVote());
        assertEquals(0, ballot1.getId());
    }

    @Test
    void testAddBallot5() {
        Vote vote = new Vote();
        vote.addBallot(new Ballot());
        LocalDateTime date = LocalDateTime.of(1, 1, 1, 1, 1);
        Vote vote1 = new Vote();
        Ballot ballot = new Ballot(date, vote1, new User());
        vote.addBallot(ballot);
        LocalDateTime date1 = LocalDateTime.of(1, 1, 1, 1, 1);
        Vote vote2 = new Vote();
        Ballot ballot1 = new Ballot(date1, vote2, new User());
        vote.addBallot(ballot1);
        assertEquals(ballot, ballot1);
        assertEquals("Ballot{id=0, date=0001-01-01T01:01, vote=0, voter=0, choices=[]}", ballot1.toString());
        assertEquals(vote, ballot1.getVote());
        assertEquals(0, ballot1.getId());
    }

    @Test
    void testAddBallot9() {
        Vote vote = new Vote();
        LocalDateTime date = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDate startDate = LocalDate.ofEpochDay(1L);
        LocalDate endDate = LocalDate.ofEpochDay(1L);
        Vote vote1 = new Vote("Label", startDate, endDate, "Algo", true, new User());
        Ballot ballot = new Ballot(date, vote1, new User());
        vote.addBallot(ballot);
        LocalDateTime date1 = LocalDateTime.of(1, 1, 1, 1, 1);
        LocalDate startDate1 = LocalDate.ofEpochDay(1L);
        LocalDate endDate1 = LocalDate.ofEpochDay(1L);
        Vote vote2 = new Vote("Label", startDate1, endDate1, "Algo", true, new User());
        Ballot ballot1 = new Ballot(date1, vote2, new User());
        vote.addBallot(ballot1);
        assertEquals(ballot, ballot1);
        assertEquals("Ballot{id=0, date=0001-01-01T01:01, vote=0, voter=0, choices=[]}", ballot1.toString());
        assertEquals(0, ballot1.getId());
    }

    @Test
    void testAddChoice() {
        Vote vote = new Vote();
        vote.addChoice(new Choice());
        assertEquals(1, vote.getChoices().size());
    }

    @Test
    void testAddChoice2() {
        Vote vote = new Vote();
        Choice choice = new Choice();
        vote.addChoice(choice);
        Choice choice1 = new Choice();
        vote.addChoice(choice1);
        assertEquals(choice, choice1);
        assertEquals(0, choice1.getId());
        assertTrue(vote.isPublic());
        assertFalse(vote.isPendingResult());
        assertFalse(vote.isIntermediaryResult());
        assertFalse(vote.isDeleted());
        assertFalse(vote.isAnonymous());
        assertEquals(1, vote.getMaxChoices());
        assertEquals(0, vote.getId());
    }

    @Test
    void testAddChoice4() {
        Vote vote = new Vote();
        ArrayList<String> names = new ArrayList<String>();
        vote.addChoice(new Choice(names, new Vote()));
        ArrayList<String> names1 = new ArrayList<String>();
        Choice choice = new Choice(names1, new Vote());
        vote.addChoice(choice);
        assertEquals(0, choice.getId());
        assertEquals("Choice{id=0, names=[], vote=0}", choice.toString());
        assertFalse(vote.isPrivate());
        assertFalse(vote.isPendingResult());
        assertFalse(vote.isIntermediaryResult());
        assertFalse(vote.isDeleted());
        assertFalse(vote.isAnonymous());
        assertEquals(1, vote.getMaxChoices());
        assertEquals(0, vote.getId());
    }

    @Test
    void testAddChoice5() {
        Vote vote = new Vote();
        vote.addChoice(new Choice());
        ArrayList<String> names = new ArrayList<String>();
        vote.addChoice(new Choice(names, new Vote()));
        ArrayList<String> names1 = new ArrayList<String>();
        Choice choice = new Choice(names1, new Vote());
        vote.addChoice(choice);
        assertEquals(0, choice.getId());
        assertEquals("Choice{id=0, names=[], vote=0}", choice.toString());
        assertFalse(vote.isPrivate());
        assertFalse(vote.isPendingResult());
        assertFalse(vote.isIntermediaryResult());
        assertFalse(vote.isDeleted());
        assertFalse(vote.isAnonymous());
        assertEquals(1, vote.getMaxChoices());
        assertEquals(0, vote.getId());
    }
    
    @Test
    void testAddChoice9() {
        Vote vote = new Vote();
        ArrayList<String> names = new ArrayList<String>();
        LocalDate startDate = LocalDate.ofEpochDay(1L);
        LocalDate endDate = LocalDate.ofEpochDay(1L);
        vote.addChoice(new Choice(names, new Vote("Label", startDate, endDate, "Algo", true, new User())));
        ArrayList<String> names1 = new ArrayList<String>();
        LocalDate startDate1 = LocalDate.ofEpochDay(1L);
        LocalDate endDate1 = LocalDate.ofEpochDay(1L);
        Choice choice = new Choice(names1, new Vote("Label", startDate1, endDate1, "Algo", true, new User()));
        vote.addChoice(choice);
        assertEquals(0, choice.getId());
        assertEquals("Choice{id=0, names=[], vote=0}", choice.toString());
    }

    @Test
    void testAddMember() {
        Vote vote = new Vote();
        vote.addMember(new User());
        assertEquals(1, vote.getMembers().size());
    }

    @Test
    void testAddMember2() {
        Vote vote = new Vote();
        vote.addMember(new User());
        User user = new User();
        vote.addMember(user);
        assertFalse(user.isConfirmed());
        assertFalse(user.isBanned());
        assertFalse(user.isAdmin());
        assertEquals(Short.SIZE, user.getSalt().length);
        assertEquals(0, user.getId());
        assertFalse(vote.isPublic());
        assertFalse(vote.isPendingResult());
        assertFalse(vote.isIntermediaryResult());
        assertFalse(vote.isDeleted());
        assertFalse(vote.isAnonymous());
        assertEquals(1, vote.getMaxChoices());
        assertEquals(0, vote.getId());
    }

    @Test
    void testIsPublic() {
        assertTrue((new Vote()).isPublic());
    }

    @Test
    void testIsPublic2() {
        Vote vote = new Vote();
        vote.addMember(new User());
        assertFalse(vote.isPublic());
    }

    @Test
    void testIsPrivate() {
        assertFalse((new Vote()).isPrivate());
    }

    @Test
    void testIsPrivate2() {
        Vote vote = new Vote();
        vote.addMember(new User());
        assertTrue(vote.isPrivate());
    }

    @Test
    void testToString() {
        Vote vote = new Vote();
        vote.setVotemaker(new User());
        assertEquals("Vote{id=0, label='null', startDate=null, endDate=null, algo='null', anonymous=false, deleted=false,"
                + " votemaker=0, choices=[], resultList=null}", vote.toString());
    }

    @Test
    void testHasResults() {
        Vote vote = new Vote();
        vote.setResultList(new ArrayList<VoteResult>());
        assertTrue(vote.hasResults());
    }

    @Test
    void testHasResults2() {
        ArrayList<VoteResult> voteResultList = new ArrayList<VoteResult>();
        voteResultList.add(new VoteResult());

        Vote vote = new Vote();
        vote.setResultList(voteResultList);
        assertFalse(vote.hasResults());
    }

    @Test
    void testCompareTo() {
        Vote vote = new Vote();
        assertEquals(0, vote.compareTo(new Vote()));
    }
}

