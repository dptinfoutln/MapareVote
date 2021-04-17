package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;

public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(Response.Status.FORBIDDEN);
    }
}
