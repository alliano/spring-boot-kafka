package com.kafka.springkafka.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Setter @Getter @ResponseStatus(code = HttpStatus.CONFLICT)
public class FailedBatchListenerException extends Exception {
    
    private static final long serialVersionUID = -4364872648328L;

    private final String message;

    public FailedBatchListenerException(String message) {
        super(message);
        this.message = message;
    }
}
