package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The type Forbidden exception.
 */
@XmlRootElement
public class ForbiddenException extends BusinessException {
    /**
     * Instantiates a new Forbidden exception.
     */
    public ForbiddenException() {
        super(MyResponse.Status.FORBIDDEN);
    }

    /**
     * Instantiates a new Forbidden exception.
     *
     * @param message the message
     */
    public ForbiddenException(String message) {
        super(MyResponse.Status.FORBIDDEN, message);
    }
}
