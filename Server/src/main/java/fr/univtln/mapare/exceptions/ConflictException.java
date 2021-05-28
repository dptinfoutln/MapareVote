package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The type Conflict exception.
 */
@XmlRootElement
public class ConflictException extends BusinessException {
    /**
     * Instantiates a new Conflict exception.
     */
    public ConflictException() {
        super(MyResponse.Status.CONFLICT);
    }

    /**
     * Instantiates a new Conflict exception.
     *
     * @param message the message
     */
    public ConflictException(String message) {
        super(MyResponse.Status.CONFLICT, message);
    }
}
