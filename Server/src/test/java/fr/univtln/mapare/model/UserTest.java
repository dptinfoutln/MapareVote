package fr.univtln.mapare.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testConstructor() {
        User actualUser = new User("jane.doe@example.org", "Doe", "Jane", "iloveyou");
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertFalse(actualUser.isConfirmed());
        assertFalse(actualUser.isBanned());
        assertFalse(actualUser.isAdmin());
        assertEquals(Short.SIZE, actualUser.getSalt().length);
        assertEquals(Short.SIZE, actualUser.getPasswordHash().length);
        assertEquals("Doe", actualUser.getLastname());
        assertEquals(0, actualUser.getId());
        assertEquals("Jane", actualUser.getFirstname());
    }

    @Test
    void testSetPassword() {
        User user = new User();
        user.setPassword("iloveyou");
        assertEquals(Short.SIZE, user.getPasswordHash().length);
    }

    @Test
    void testAddStartedVote() {
        User user = new User();
        user.addStartedVote(new Vote());
        assertEquals(1, user.getStartedVotes().size());
    }

    @Test
    void testAddStartedVote2() {
        User user = new User();
        user.addStartedVote(new Vote());
        Vote vote = new Vote();
        user.addStartedVote(vote);
        assertTrue(vote.isPublic());
        assertFalse(vote.isPendingResult());
        assertFalse(vote.isIntermediaryResult());
        assertFalse(vote.isDeleted());
        assertFalse(vote.isAnonymous());
        assertEquals(1, vote.getMaxChoices());
        assertEquals(0, vote.getId());
        assertFalse(user.isConfirmed());
        assertFalse(user.isBanned());
        assertFalse(user.isAdmin());
        assertEquals(Short.SIZE, user.getSalt().length);
        assertEquals(0, user.getId());
    }

    @Test
    void testAddPrivateVote() {
        User user = new User();
        user.addPrivateVote(new Vote());
        assertEquals(1, user.getPrivateVoteList().size());
    }

    @Test
    void testAddPrivateVote2() {
        User user = new User();
        user.addPrivateVote(new Vote());
        Vote vote = new Vote();
        user.addPrivateVote(vote);
        assertTrue(vote.isPublic());
        assertFalse(vote.isPendingResult());
        assertFalse(vote.isIntermediaryResult());
        assertFalse(vote.isDeleted());
        assertFalse(vote.isAnonymous());
        assertEquals(1, vote.getMaxChoices());
        assertEquals(0, vote.getId());
        assertFalse(user.isConfirmed());
        assertFalse(user.isBanned());
        assertFalse(user.isAdmin());
        assertEquals(Short.SIZE, user.getSalt().length);
        assertEquals(0, user.getId());
    }

    @Test
    void testAddVotedVote() {
        User user = new User();
        user.addVotedVote(new VotedVote());
        assertEquals(1, user.getVotedVotes().size());
    }

    @Test
    void testAddVotedVote2() {
        User user = new User();
        user.addVotedVote(new VotedVote());
        user.addVotedVote(new VotedVote());
        assertFalse(user.isConfirmed());
        assertFalse(user.isBanned());
        assertFalse(user.isAdmin());
        assertEquals(Short.SIZE, user.getSalt().length);
        assertEquals(0, user.getId());
    }

    @Test
    void testAddVotedVote3() {
        User user = new User();
        user.addVotedVote(new VotedVote());

        VotedVote votedVote = new VotedVote();
        votedVote.setUser(new User());
        user.addVotedVote(votedVote);
        assertEquals(2, user.getVotedVotes().size());
    }

    @Test
    void testCheckPassword() {
        assertFalse((new User()).checkPassword("iloveyou"));
        assertTrue((new User("jane.doe@example.org", "Doe", "Jane", "iloveyou")).checkPassword("iloveyou"));
    }

    @Test
    void testGetName() {
        assertEquals("null, null <null>", (new User()).getName());
    }

    @Test
    void testToString() {
        assertEquals("User{id=0, email='null', lastname='null', firstname='null'}", (new User()).toString());
    }

    @Test
    void testGetVotesOnWhichTheUserHasVoted() {
        assertTrue((new User()).getVotesOnWhichTheUserHasVoted().isEmpty());
    }

    @Test
    void testGetVotesOnWhichTheUserHasVoted2() {
        User user = new User();
        user.addVotedVote(new VotedVote());
        assertEquals(1, user.getVotesOnWhichTheUserHasVoted().size());
    }
}

