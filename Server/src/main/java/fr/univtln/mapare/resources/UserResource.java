package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.controllers.Controllers;
import fr.univtln.mapare.model.User;
import fr.univtln.mapare.model.Vote;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("users")
public class UserResource {
//    static Controller<User> ctrl = new Controller<>();
    static int lastId = -1; // init at highest ID + 1

    public UserResource() {
        if (lastId == -1) {
            Controllers.loadUsers();
            int maxi = Controllers.Users.getList().stream().max(Comparator.comparingInt(User::getId)).get().getId();
            lastId = maxi + 1;
        }
    }

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
        return Controllers.Users.getList();
    }

    @GET
    @Path("{id}")
    public User getUser(@PathParam("id") int id) {
        return Controllers.Users.mapGet(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User addUser(User user) {
        user.setId(lastId);
        Controllers.Users.mapAdd(user.getId(), user);
        return user;
    }
}
