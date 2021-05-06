package fr.univtln.mapare.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.io.Serializable;

/**
 * The type Business exception is our main HATEOS exception type.
 * It adds a HTTP Status.
 */
@Getter
@JsonIgnoreProperties({"stackTrace"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)

@XmlRootElement
public class BusinessException extends Exception implements Serializable {
    /**
     * The Status.
     */
    final MyResponse.Status status;

    /**
     * Instantiates a new Business exception with the default message.
     *
     * @param status the status
     */
    public BusinessException(MyResponse.Status status) {
        super(status.getReasonPhrase());
        this.status = status;
    }

    /**
     * Instantiates a new Business exception with the default message.
     *
     * @param status the status
     * @param message the message
     */
    public BusinessException(MyResponse.Status status,String message) {
        super(message);
        this.status = status;
    }
}