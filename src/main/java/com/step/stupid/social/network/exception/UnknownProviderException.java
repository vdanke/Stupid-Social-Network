package com.step.stupid.social.network.exception;

public class UnknownProviderException extends RuntimeException {
    public UnknownProviderException(String provider) {
        super(provider);
    }
}
