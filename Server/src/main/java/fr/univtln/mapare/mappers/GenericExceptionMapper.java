package fr.univtln.mapare.mappers;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * The type Generic exception mapper.
 */
@SuppressWarnings("unused")
@Provider
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    final Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

    public Response toResponse(Exception ex) {
        ex.printStackTrace();
        return Response.status(status)
                .entity(ex.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
