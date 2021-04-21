package fr.univtln.mapare.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(Response.Status.FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(Response.Status.FORBIDDEN, message);
    }
}
