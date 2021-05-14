package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConflictException extends BusinessException {
    public ConflictException() {
        super(MyResponse.Status.CONFLICT);
    }

    public ConflictException(String message) {
        super(MyResponse.Status.CONFLICT, message);
    }
}
