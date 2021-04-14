package fr.univtln.mapare.resources;

import fr.univtln.mapare.controllers.Controller;
import fr.univtln.mapare.model.Choice;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.Collection;

@Path("choices")
public class ChoiceResource {
    Controller<Choice> ctrl = new Controller<>();

    @GET
    public Collection<Choice> getChoices(@QueryParam("page_num") int pagenum,
                                         @QueryParam("page_size") int pagesize) {
        return ctrl.getList();
    }
}
