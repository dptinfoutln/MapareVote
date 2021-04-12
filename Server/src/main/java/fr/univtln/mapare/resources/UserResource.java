package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("users")
public class UserResource {
    Controller<User> ctrl = new Controller<>();

    @GET
    public List<User> getUsers(@QueryParam("page_num") int pagenum,
                               @QueryParam("page_size") int pagesize) {
        //Lancer DAO
        //Pagination
        //rentrer users dans liste
        ctrl.listAdd(new User(1, "test@example.com", "Dupont", "Thomas", "TESTX5", true, true, false));
        return ctrl.getList();
    }
}
