package com.step.stupid.social.network.exception;

public class OAuth2AuthorizationProcessingException extends RuntimeException {
    public OAuth2AuthorizationProcessingException(String emptyAuthorization) {
        super(emptyAuthorization);
    }
}
