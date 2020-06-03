package com.step.stupid.social.network.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SerializationJsonException extends RuntimeException {
    public SerializationJsonException(String message) {
        super(message);
    }

    public SerializationJsonException(String format, JsonProcessingException e) {
        super(format, e);
    }
}
