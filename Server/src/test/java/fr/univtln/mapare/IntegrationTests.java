package fr.univtln.mapare;

import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.exceptions.BusinessException;
import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Log
class IntegrationTests {
    private static EntityManagerFactory TEST_EMF;

    /**
     * Init tests.
     */
    @BeforeAll
    static void initTests() {
        TEST_EMF = Persistence.createEntityManagerFactory("test-pu");
    }

    @Test
    void createUser() throws BusinessException {
        EntityManager TEST_EM = TEST_EMF.createEntityManager();
        User sent = User.builder().email("tests").password("coucou")
                .firstname("user").lastname("test").build();
        UserDAO.of(TEST_EM).persist(sent);
        List<User> users = UserDAO.of(TEST_EM).findAll();
        assertEquals(1, users.size());

        User received = users.get(0);
        assertNotEquals(0,received.getId());
        assertEquals(sent.getId(), received.getId());

        TEST_EM.close();
    }
}
