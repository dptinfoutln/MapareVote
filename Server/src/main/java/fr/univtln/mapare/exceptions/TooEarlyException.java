package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The type Too early exception.
 */
@XmlRootElement
public class TooEarlyException extends BusinessException {
    /**
     * Instantiates a new Too early exception.
     */
    public TooEarlyException() {
        super(MyResponse.Status.TOO_EARLY);
    }

    /**
     * Instantiates a new Too early exception.
     *
     * @param message the message
     */
    public TooEarlyException(String message) {
        super(MyResponse.Status.TOO_EARLY, message);
    }
}
