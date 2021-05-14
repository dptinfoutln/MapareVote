package fr.univtln.mapare;

import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.dao.VoteDAO;
import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.model.Choice;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Log
class EntityIT {
    private static EntityManagerFactory TEST_EMF;

    /**
     * Init tests.
     */
    @BeforeAll
    static void initTests() {
        TEST_EMF = Persistence.createEntityManagerFactory("test-pu");
    }

    @Test
    void userCreation() throws BusinessException {
        EntityManager TEST_EM = TEST_EMF.createEntityManager();
        User sent = new User();
        UserDAO dao = UserDAO.of(TEST_EM);

        assertThrows(RollbackException.class, () -> dao.persist(sent));
        sent.setEmail("tests");
        assertThrows(RollbackException.class, () -> dao.persist(sent));
        sent.setFirstname("user");
        assertThrows(RollbackException.class, () -> dao.persist(sent));
        sent.setLastname("test");
        assertThrows(RollbackException.class, () -> dao.persist(sent));
        sent.setPassword("coucou");
        dao.persist(sent);

        List<User> users = dao.findAll();
        assertEquals(1, users.size());

        User received = users.get(0);
        assertNotEquals(0,received.getId());
        assertEquals(sent.getId(), received.getId());
        assertEquals(sent.getEmail(), received.getEmail());
        assertEquals(sent.getEmailToken(), received.getEmailToken());
        assertEquals(sent.getFirstname(), received.getFirstname());
        assertEquals(sent.getLastname(), received.getLastname());
        assertEquals(sent.getSalt(), received.getSalt());
        assertEquals(sent.getPasswordHash(), received.getPasswordHash());

        assertTrue(received.checkPassword("coucou"));

        User emailUniquenessTestUser = User.builder().email("tests").password("indeed")
                .firstname("uniqueness").lastname("testuser").build();

        assertThrows(RollbackException.class, () -> dao.persist(emailUniquenessTestUser));

        dao.remove(sent.getId());

        TEST_EM.close();
    }

    @Test
    void voteCreation() throws BusinessException {
        EntityManager TEST_EM = TEST_EMF.createEntityManager();
        UserDAO userDAO = UserDAO.of(TEST_EM);
        VoteDAO voteDAO = VoteDAO.of(TEST_EM);

        User creator = User.builder().email("bendy").password("andthe")
                .firstname("ink").lastname("machine").build();

        Vote sent = new Vote();

        assertThrows(ConflictException.class, () -> voteDAO.persist(sent));
        sent.setAlgo("majority");
        assertThrows(ConflictException.class, () -> voteDAO.persist(sent));
        sent.setAnonymous(false);
        assertThrows(ConflictException.class, () -> voteDAO.persist(sent));
        sent.setLabel("Testvote");
        assertThrows(ConflictException.class, () -> voteDAO.persist(sent));
        sent.setStartDate(LocalDate.now());
        assertThrows(ConflictException.class, () -> voteDAO.persist(sent));
        sent.setVotemaker(creator);
        assertThrows(RollbackException.class, () -> voteDAO.persist(sent));
        sent.addChoice(Choice.builder().names(Collections.singletonList("choice1")).vote(sent).build());
        assertThrows(RollbackException.class, () -> voteDAO.persist(sent));
        sent.addMember(creator);



//        creator.addStartedVote(sent);



//        System.out.println(sent);

        userDAO.persist(creator);
        voteDAO.persist(sent);

        System.out.println(creator.getPrivateVoteList().size());

        System.out.println(userDAO.findAll());

//        System.out.println(sent);
        assertTrue(sent.isPrivate());

        List<Vote> votes = voteDAO.findAll();
        List<User> users = userDAO.findAll();

        assertEquals(1, votes.size());
        assertEquals(1, users.size());

        Vote received = votes.get(0);

        assertNotEquals(0,received.getId());
        assertEquals(sent.getId(), received.getId());
        assertEquals(sent.getAlgo(), received.getAlgo());
        assertEquals(sent.getChoices(), received.getChoices());
        assertEquals(sent.getMembers(), received.getMembers());
        assertEquals(sent.getEndDate(), received.getEndDate());
        assertEquals(sent.getLabel(), received.getLabel());
        assertEquals(sent.getMaxChoices(), received.getMaxChoices());
        assertEquals(sent.getStartDate(), received.getStartDate());
        assertEquals(sent.getVotemaker(), received.getVotemaker());

        System.out.println(creator.getStartedVotes());

        userDAO.remove(creator.getId());
        System.out.println(voteDAO.findAll());

        TEST_EM.close();
    }
}
