package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The type Not found exception.
 */
@XmlRootElement
public class NotFoundException extends BusinessException {
    /**
     * Instantiates a new Not found exception.
     */
    public NotFoundException() {
        super(MyResponse.Status.NOT_FOUND);
    }

    /**
     * Instantiates a new Not found exception.
     *
     * @param message the message
     */
    public NotFoundException(String message) {
        super(MyResponse.Status.NOT_FOUND, message);
    }
}
