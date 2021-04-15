package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;

public class NotFoundException extends BusinessException {
    public NotFoundException() {
        super(Response.Status.NOT_FOUND);
    }
}