package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The type Unauthorized exception.
 */
@XmlRootElement
public class UnauthorizedException extends BusinessException {
    /**
     * Instantiates a new Unauthorized exception.
     */
    public UnauthorizedException() {
        super(MyResponse.Status.UNAUTHORIZED);
    }

    /**
     * Instantiates a new Unauthorized exception.
     *
     * @param message the message
     */
    public UnauthorizedException(String message) {
        super(MyResponse.Status.UNAUTHORIZED, message);
    }
}
