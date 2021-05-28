package fr.univtln.mapare.mappers;

import fr.univtln.mapare.exceptions.BusinessException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * The type Business exception mapper.
 */
@SuppressWarnings("unused")
@Provider
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    public Response toResponse(BusinessException ex) {
        return Response.status(ex.getStatus())
                .entity(ex.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
