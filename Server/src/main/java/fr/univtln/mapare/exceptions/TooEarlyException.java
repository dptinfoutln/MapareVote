package fr.univtln.mapare.exceptions;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TooEarlyException extends BusinessException{
    public TooEarlyException() {
        super(MyResponse.Status.TOO_EARLY);
    }

    public TooEarlyException(String message) {
        super(MyResponse.Status.TOO_EARLY, message);
    }
}
