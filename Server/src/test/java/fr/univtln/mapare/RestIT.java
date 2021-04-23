package fr.univtln.mapare;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;

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
    }

    /**
     * Stops the application at the end of the test.
     */
    @AfterClass
    public static void tearDown() {
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
        webTarget.path("users").request(MediaType.APPLICATION_JSON_TYPE).get();

    }
}
