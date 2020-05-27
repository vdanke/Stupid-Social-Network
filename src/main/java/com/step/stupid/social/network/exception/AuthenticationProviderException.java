package com.step.stupid.social.network.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.Map;

public class AuthenticationProviderException extends AuthenticationException {
    private OAuth2AuthorizationRequest request;

    public AuthenticationProviderException(String message) {
        super(message);
    }

    public AuthenticationProviderException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationProviderException(String authorizationFailed, OAuth2AuthorizationRequest request) {
        super(authorizationFailed);
        this.request = request;
    }

    public Map<String, Object> getEmail() {
        return request.getAttributes();
    }
}
