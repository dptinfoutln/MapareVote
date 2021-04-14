package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("users")
public class UserResource {
    static Controller<User> ctrl = new Controller<>();
    static int lastId = 0; // init at highest ID + 1

    //preload list

    @GET
    public List<User> getUsers(@QueryParam("page_num") int pagenum,
                               @QueryParam("page_size") int pagesize) {
        //Lancer DAO
        //Pagination
        //rentrer users dans liste
        //ctrl.listAdd(new User(1, "test@example.com", "Dupont", "Thomas", "TESTX5", true, true, false));
        return ctrl.getList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User addUser(User user) {
        user.setId(lastId++);
        ctrl.listAdd(user);
        return user;
    }
}
