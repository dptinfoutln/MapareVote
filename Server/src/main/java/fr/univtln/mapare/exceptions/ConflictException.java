package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConflictException extends BusinessException {
    public ConflictException() {
        super(Response.Status.CONFLICT);
    }

    public ConflictException(String message) {
        super(Response.Status.CONFLICT, message);
    }
}
