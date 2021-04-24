package fr.univtln.mapare;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.model.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

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
        // create the client
        Client client = ClientBuilder.newClient();
        webTarget = client.target(Main.BASE_URI);
        Controllers.init();
    }

    /**
     * Stops the application at the end of the test.
     */
    @AfterClass
    public static void tearDown() {
        httpServer.shutdown();
        Controllers.close();
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
//        webTarget.path("users").request(MediaType.APPLICATION_JSON).post(
//                Entity.entity("{\"email\":\"adresse@mail.com\",\"lastname\":\"Nomdefamille\",\"firstname\":\"Prenom\",\"password\":\"mdp\"}", MediaType.APPLICATION_JSON));
        //TODO: use a dev db for tests
        //TODO: get the id of the one just created
        //TODO: check the fields
        //TODO: remove newly created at the end
        User user = webTarget.path("users/15").request(MediaType.APPLICATION_JSON).get(new GenericType<User>() {});
//        User temp = response.readEntity(User.class);
//        System.out.println(response.readEntity(new GenericType<List<User>>() {}));
        System.out.println(user);
    }
}
