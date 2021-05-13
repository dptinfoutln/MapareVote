package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(MyResponse.Status.FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(MyResponse.Status.FORBIDDEN, message);
    }
}
