package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotFoundException extends BusinessException {
    public NotFoundException() {
        super(MyResponse.Status.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(MyResponse.Status.NOT_FOUND, message);
    }
}
