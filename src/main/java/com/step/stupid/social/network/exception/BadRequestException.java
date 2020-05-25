package com.step.stupid.social.network.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String badRequest) {
        super(badRequest);
    }
}
