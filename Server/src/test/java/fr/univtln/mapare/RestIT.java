package fr.univtln.mapare;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class RestIT {
    private static HttpServer httpServer;

    private static WebTarget webTarget;

    /**
     * Starts the application before the tests.
     */
    @BeforeClass
    public static void setUp() {
        //start the Grizzly2 web container
        httpServer = Main.startServer();
        Controllers.testinit();
        // create the client
        Client client = ClientBuilder.newClient();
        webTarget = client.target(Main.BASE_URI);
    }

    /**
     * Stops the application at the end of the test.
     */
    @AfterClass
    public static void tearDown() {
        Controllers.close();
        httpServer.shutdown();
    }

    @Before
    public void beforeEach() {
        //webTarget.path("library/init").request().put(Entity.entity("", MediaType.TEXT_PLAIN));
    }

    @After
    public void afterEach() {
        //webTarget.path("authors").request().delete();
    }

    @Test
    public void testCreateAccount() {
        Response response = webTarget.path("users").request(MediaType.APPLICATION_JSON).get();

        List<User> beforeList = response.readEntity(List.class);

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"carlorff@hotmail.fr\",\"firstname\":\"carl\",\"password\":\"ofortuna\"}",
                        MediaType.APPLICATION_JSON));

        assertEquals(403, response.getStatus());

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"lastname\":\"orff\",\"firstname\":\"carl\",\"password\":\"ofortuna\"}",
                        MediaType.APPLICATION_JSON));

        assertEquals(403, response.getStatus());

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"carlorff@hotmail.fr\",\"lastname\":\"orff\",\"password\":\"ofortuna\"}",
                        MediaType.APPLICATION_JSON));

        assertEquals(403, response.getStatus());

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"carlorff@hotmail.fr\",\"lastname\":\"orff\",\"firstname\":\"carl\"}",
                        MediaType.APPLICATION_JSON));

        assertEquals(403, response.getStatus());

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"carlorff@hotmail.fr\",\"lastname\":\"orff\",\"firstname\":\"carl\",\"password\":\"ofortuna\"}",
                        MediaType.APPLICATION_JSON));

        User carlorff = response.readEntity(User.class);

        assertEquals("carlorff@hotmail.fr", carlorff.getEmail());
        assertEquals("orff", carlorff.getLastname());
        assertEquals("carl", carlorff.getFirstname());

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"carlorff@hotmail.fr\",\"lastname\":\"orff\",\"firstname\":\"carl\",\"password\":\"ofortuna\"}",
                        MediaType.APPLICATION_JSON));

        assertEquals(409, response.getStatus());

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON).get();

        List<User> afterList = response.readEntity(List.class);

        assertEquals(beforeList.size() + 1, afterList.size());

        User carlorff2 = UserDAO.of(Controllers.getEntityManager()).findById(carlorff.getId());
        assertTrue(carlorff2.checkPassword("ofortuna"));

        webTarget.path("users/" + carlorff.getId()).request(MediaType.APPLICATION_JSON).delete();

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON).get();

        List<User> deletedList = response.readEntity(List.class);

        assertEquals(beforeList.size(), deletedList.size());

        System.out.println(beforeList);
    }

    @Test
    public void deleteCarl() {
        UserDAO.of(Controllers.getEntityManager()).remove(UserDAO.of(Controllers.getEntityManager()).findByEmail("carlorff@hotmail.fr"));

        assertTrue(true);
    }
}
