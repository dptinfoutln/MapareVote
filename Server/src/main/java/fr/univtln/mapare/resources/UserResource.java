package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.PrivateVote;
import fr.univtln.mapare.model.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;

@Path("users")
public class UserResource {
    static Controller<User> ctrl = new Controller<>();
    static int lastId = 0; // init at highest ID + 1

    //preload list

    private static void foo() {
        lastId++;
    }

    @GET
    public Collection<User> getUsers(@DefaultValue("1") @QueryParam("page_num") int pagenum,
                                     @DefaultValue("20") @QueryParam("page_size") int pagesize) {
        //Lancer DAO
        //Pagination
        //rentrer users dans liste
        //ctrl.listAdd(new User(1, "test@example.com", "Dupont", "Thomas", "TESTX5", true, true, false));
        return ctrl.getList();
    }

    @GET
    @Path("{id}")
    public User getUser(@PathParam("id") int id) {
        return ctrl.mapGet(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User addUser(User user) {
        user.setId(lastId);
        ctrl.mapAdd(user.getId(), user);
        return user;
    }
}
