package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.AuthController;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.json.JSONObject;

@Path("old/auth")
public class AuthRessourceOld {

    @POST
    @Path("signin")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String signIn(String strJsonLogins) throws Exception {
        JSONObject jsonLogins = new JSONObject(strJsonLogins);
        return AuthController.signIn(
                jsonLogins.getString("email"),
                jsonLogins.getString("password")
        );
    }

    @POST
    @Path("signup")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public void signUp(String strJsonLogins){
        JSONObject jsonLogins = new JSONObject(strJsonLogins);
        AuthController.signUp(
                jsonLogins.getString("email"),
                jsonLogins.getString("password")
        );
    }

    @POST
    @Path("signout")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public void signOut(String strJsonToken) {
        JSONObject jsonToken = new JSONObject(strJsonToken);
        AuthController.signOut(
                jsonToken.getString("token")
        );
    }
}

