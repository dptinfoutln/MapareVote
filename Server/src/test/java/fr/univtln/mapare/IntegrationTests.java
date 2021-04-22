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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Log
public class IntegrationTests {
    private static EntityManagerFactory TEST_EMF;

    /**
     * Init tests.
     */
    @BeforeAll
    public static void initTests() {
        TEST_EMF = Persistence.createEntityManagerFactory("test-pu");
    }

    @Test
    public void createUser() throws BusinessException {
        EntityManager TEST_EM = TEST_EMF.createEntityManager();
        User test = User.builder().email("tests").password("coucou")
                .firstname("user").lastname("test").build();
        UserDAO.of(TEST_EM).persist(test);
        User test2 = UserDAO.of(TEST_EM).findAll().get(0);
        assertNotEquals(0,test2.getId());
        TEST_EM.close();
    }
}
