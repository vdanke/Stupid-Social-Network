package com.step.stupid.social.network.exception;

public class MessageSendException extends RuntimeException {
    public MessageSendException(String errorMessage) {
        super(errorMessage);
    }
}
