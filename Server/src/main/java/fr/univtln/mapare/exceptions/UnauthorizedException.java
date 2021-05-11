package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(MyResponse.Status.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(MyResponse.Status.UNAUTHORIZED, message);
    }
}
