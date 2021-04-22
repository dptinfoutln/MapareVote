package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotFoundException extends BusinessException {
    public NotFoundException() {
        super(Response.Status.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(Response.Status.NOT_FOUND, message);
    }
}
