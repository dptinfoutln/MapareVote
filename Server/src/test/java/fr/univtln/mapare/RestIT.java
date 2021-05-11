package fr.univtln.mapare;

import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.dao.UserDAO;
import fr.univtln.mapare.exceptions.ConflictException;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
    public void createAccountTest() {
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

        String token = webTarget.path("auth/signin")
                .request()
                .accept(MediaType.TEXT_PLAIN)
                .header("Authorization",  "Basic " + java.util.Base64.getEncoder()
                        .encodeToString(("carlorff@hotmail.fr:ofortuna").getBytes()))
                .get(String.class);

        webTarget.path("users/me")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get();

        webTarget.path("users/" + carlorff.getId()).request(MediaType.APPLICATION_JSON).delete();

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON).get();

        List<User> deletedList = response.readEntity(List.class);

        assertEquals(beforeList.size(), deletedList.size());
    }

    @Test
    public void createVoteTest() {
        Response response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"carlorff@hotmail.fr\",\"lastname\":\"orff\",\"firstname\":\"carl\",\"password\":\"ofortuna\"}",
                        MediaType.APPLICATION_JSON));

        User carlorff = response.readEntity(User.class);

        String token;

        response = webTarget.path("auth/signin")
                .request()
                .accept(MediaType.TEXT_PLAIN)
                .header("Authorization",  "Basic " + java.util.Base64.getEncoder()
                        .encodeToString(("carlorff@hotmail.fr:ofortuna").getBytes()))
                .get();

        token = response.readEntity(String.class);

        String todaysdate, dateintendays;

        todaysdate = LocalDate.now() + "";

        dateintendays = LocalDate.now().plusDays(10) + "";

        String[] tempArray = todaysdate.split("-");
        for (int i = 1; i < 3; i++)
            if (tempArray[i].charAt(0) == '0')
                tempArray[i] = String.valueOf(tempArray[i].charAt(1));
        todaysdate = "[" + tempArray[0] + "," + tempArray[1] + "," + tempArray[2] + "]";

        tempArray = dateintendays.split("-");
        for (int i = 1; i < 3; i++)
            if (tempArray[i].charAt(0) == '0')
                tempArray[i] = String.valueOf(tempArray[i].charAt(1));
        dateintendays = "[" + tempArray[0] + "," + tempArray[1] + "," + tempArray[2] + "]";

        webTarget.path("votes/public").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .post(Entity.entity(
                        "{\"label\":\"Meilleur composition musicale\",\"startDate\":" + todaysdate +
                        ",\"endDate\":" + dateintendays +
                                ",\"algo\":\"majority\",\"anonymous\":false,\"choices\":" +
                                "[{\"names\":[\"Carmina Burana\"]},{\"names\":[\"Catulli Carmina\"]}," +
                                "{\"names\":[\"Trionfo di Afrodite\"]}],\"maxChoices\":\"1\"}"
                , MediaType.APPLICATION_JSON));

        response = webTarget.path("votes/public").request(MediaType.APPLICATION_JSON).get();

        List<Vote> voteList = response.readEntity(List.class);

        assertEquals(1, voteList.size());

        webTarget.path("votes/public").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .post(Entity.entity(
                        "{\"label\":\"Test\",\"startDate\":" + todaysdate +
                                ",\"endDate\":" + dateintendays +
                                ",\"algo\":\"majority\",\"anonymous\":false,\"choices\":" +
                                "[{\"names\":[\"Carmina Burana\"]},{\"names\":[\"Catulli Carmina\"]}," +
                                "{\"names\":[\"Trionfo di Afrodite\"]}],\"maxChoices\":\"1\"}"
                        , MediaType.APPLICATION_JSON));

        voteList = webTarget.path("votes/public").queryParam("starts_with","Te").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(1, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("starts_with","X").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(0, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("name_like","Test").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(1, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("name_like","ofortuna").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(0, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("name_like","e").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(2, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("ends_with","st").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(1, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("ends_with","X").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(0, voteList.size());

        voteList = webTarget.path("votes/public")
                .queryParam("stars_with", "Te")
                .queryParam("ends_with","st")
                .request(MediaType.APPLICATION_JSON)
                .get(List.class);

        assertEquals(1, voteList.size());

        voteList = webTarget.path("votes/public")
                .queryParam("stars_with", "Me")
                .queryParam("name_like", "composition")
                .queryParam("ends_with","le")
                .request(MediaType.APPLICATION_JSON)
                .get(List.class);

        assertEquals(1, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("algo","majority").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(2, voteList.size());

        voteList = webTarget.path("votes/public").queryParam("algo","majorit").request(MediaType.APPLICATION_JSON).get(List.class);

        assertEquals(0, voteList.size());

        webTarget.path("votes/private").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .post(Entity.entity(
                        "{\"label\":\"Privatvot\",\"startDate\":" + todaysdate +
                                ",\"endDate\":" + dateintendays +
                                ",\"algo\":\"majority\",\"anonymous\":false,\"choices\":" +
                                "[{\"names\":[\"Carmina Burana\"]},{\"names\":[\"Catulli Carmina\"]}," +
                                "{\"names\":[\"Trionfo di Afrodite\"]}],\"maxChoices\":\"1\"}"
                        , MediaType.APPLICATION_JSON));


        voteList = webTarget.path("votes/private/invited").request(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token).get(List.class);

        assertEquals(1, voteList.size());

        webTarget.path("users/me")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get();

        webTarget.path("users/" + carlorff.getId()).request(MediaType.APPLICATION_JSON).delete();

        response = webTarget.path("votes/public").request(MediaType.APPLICATION_JSON).get();

        voteList = response.readEntity(List.class);

        assertEquals(0, voteList.size());
    }

    @Test
    public void votingTest() throws InterruptedException {
        Response response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"carlorff@hotmail.fr\",\"lastname\":\"orff\",\"firstname\":\"carl\",\"password\":\"ofortuna\"}",
                        MediaType.APPLICATION_JSON));

        User carlorff = response.readEntity(User.class);

        String token;

        response = webTarget.path("auth/signin")
                .request()
                .accept(MediaType.TEXT_PLAIN)
                .header("Authorization",  "Basic " + java.util.Base64.getEncoder()
                        .encodeToString(("carlorff@hotmail.fr:ofortuna").getBytes()))
                .get();

        token = response.readEntity(String.class);

        String todaysdate, dateintendays;

        webTarget.path("users/me")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get();

        todaysdate = LocalDate.now() + "";

        dateintendays = LocalDate.now().plusDays(10) + "";

        String[] tempArray = todaysdate.split("-");
        for (int i = 1; i < 3; i++)
            if (tempArray[i].charAt(0) == '0')
                tempArray[i] = String.valueOf(tempArray[i].charAt(1));
        todaysdate = "[" + tempArray[0] + "," + tempArray[1] + "," + tempArray[2] + "]";

        tempArray = dateintendays.split("-");
        for (int i = 1; i < 3; i++)
            if (tempArray[i].charAt(0) == '0')
                tempArray[i] = String.valueOf(tempArray[i].charAt(1));
        dateintendays = "[" + tempArray[0] + "," + tempArray[1] + "," + tempArray[2] + "]";

        response = webTarget.path("votes/public").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .post(Entity.entity(
                        "{\"label\":\"Meilleur composition musicale\",\"startDate\":" + todaysdate +
                                ",\"endDate\":" + dateintendays +
                                ",\"algo\":\"STV\",\"anonymous\":false,\"intermediaryResult\":true,\"choices\":"
                                + "[{\"names\":[\"Carmina Burana\"]},{\"names\":[\"Catulli Carmina\"]}," +
                                "{\"names\":[\"Trionfo di Afrodite\"]}],\"maxChoices\":\"1\"}"
                        , MediaType.APPLICATION_JSON));

        response = webTarget.path("votes/public").request(MediaType.APPLICATION_JSON).get();

        List<Vote> voteList = response.readEntity(List.class);

        System.out.println(voteList);

        Vote vote = webTarget.path("votes/" + voteList.toString().charAt(5))
                .request(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get(Vote.class);

        System.out.println(vote);

        int delay = 250;

//        Thread.sleep(delay);

        webTarget.path("votes/" + vote.getId() + "/ballots").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .post(Entity.entity(
                        "{\"id\":\"0\",\"choices\":[{\"choice\":{\"id\":" + vote.getChoices().get(0).getId() +
                                "},\"weight\":1, \"ballot\":\"0\"}, {\"choice\":{\"id\":" +
                                vote.getChoices().get(1).getId() +
                                "},\"weight\":2, \"ballot\":\"0\"}, {\"choice\":{\"id\":" +
                                vote.getChoices().get(2).getId() +
                                "},\"weight\":3, \"ballot\":\"0\"}]}"
                        , MediaType.APPLICATION_JSON));

        webTarget.path("votes/" + vote.getId() + "/myballot").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get();

        webTarget.path("votes/" + vote.getId())
                .request(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get();

        webTarget.path("users").request(MediaType.APPLICATION_JSON).get();

//        Thread.sleep(delay);

        response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(
                        "{\"email\":\"tchaikovsky@hotmail.fr\",\"lastname\":\"tchaikovsky\",\"firstname\":\"pyotr\",\"password\":\"1812\"}",
                        MediaType.APPLICATION_JSON));

        User tchaikovsky = response.readEntity(User.class);

        webTarget.path("users/me")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get();

        token = webTarget.path("auth/signin")
                .request()
                .accept(MediaType.TEXT_PLAIN)
                .header("Authorization",  "Basic " + java.util.Base64.getEncoder()
                        .encodeToString(("tchaikovsky@hotmail.fr:1812").getBytes()))
                .get(String.class);

        webTarget.path("votes/" + vote.getId() + "/results")
                .request(MediaType.APPLICATION_JSON)
                .header( "Authorization",  "Bearer " + token)
                .get();

        webTarget.path("users/" + carlorff.getId()).request(MediaType.APPLICATION_JSON).delete();

        webTarget.path("users/" + tchaikovsky.getId()).request(MediaType.APPLICATION_JSON).delete();
    }

    //Note: We can't test patch verbs for some reason.
//    @Test
//    public void banUser() {
//        Response response = webTarget.path("users").request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(
//                        "{\"email\":\"carlorff@hotmail.fr\",\"lastname\":\"orff\",\"firstname\":\"carl\",\"password\":\"ofortuna\"}",
//                        MediaType.APPLICATION_JSON));
//
//        User carlorff = response.readEntity(User.class);
//
//        String carltoken;
//
//        response = webTarget.path("auth/signin")
//                .request()
//                .accept(MediaType.TEXT_PLAIN)
//                .header("Authorization",  "Basic " + java.util.Base64.getEncoder()
//                        .encodeToString(("carlorff@hotmail.fr:ofortuna").getBytes()))
//                .get();
//
//        carltoken = response.readEntity(String.class);
//
//        webTarget.path("users").request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(
//                        "{\"email\":\"tchaikovsky@hotmail.fr\",\"lastname\":\"tchaikovsky\",\"firstname\":\"pyotr\",\"password\":\"1812\"}",
//                        MediaType.APPLICATION_JSON));
//
//        String pyotrtoken = webTarget.path("auth/signin")
//                .request()
//                .accept(MediaType.TEXT_PLAIN)
//                .header("Authorization",  "Basic " + java.util.Base64.getEncoder()
//                        .encodeToString(("tchaikovsky@hotmail.fr:1812").getBytes()))
//                .get(String.class);
//
//        User tchaikovsky = webTarget.path("users/me")
//                .request(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .header( "Authorization",  "Bearer " + pyotrtoken)
//                .get(User.class);
//
//        response = webTarget.path("users/" + tchaikovsky.getId() + "/ban")
//                .request()
//                .header( "Authorization",  "Bearer " + carltoken)
//                .patch();
//
//        assertEquals(403, response.getStatus());
//
//
//        UserDAO dao = UserDAO.of(Controllers.getEntityManager());
//        User temppyotr = dao.findById(tchaikovsky.getId());
//        temppyotr.setAdmin(true);
//        dao.update(temppyotr);
//
//        response = webTarget.path("users/" + carlorff.getId() + "/ban")
//                .request()
//                .header( "Authorization",  "Bearer " + pyotrtoken)
//                .patch();
//
//        assertEquals(200, response.getStatus());
//
//        User tempcarl = dao.findById(carlorff.getId());
//
//        assertTrue(tempcarl.isBanned());
//
//        webTarget.path("users/" + carlorff.getId()).request(MediaType.APPLICATION_JSON).delete();
//
//        webTarget.path("users/" + tchaikovsky.getId()).request(MediaType.APPLICATION_JSON).delete();
//    }
}
