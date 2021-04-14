package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;

public class IllegalArgumentException extends BusinessException {
    public IllegalArgumentException() {
        super(Response.Status.NOT_ACCEPTABLE);
    }
}
