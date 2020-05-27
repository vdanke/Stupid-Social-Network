package com.step.stupid.social.network.configuration.security.oauth2;

import com.step.stupid.social.network.exception.AuthenticationProviderException;
import com.step.stupid.social.network.service.CookieOperation;
import com.step.stupid.social.network.service.SerializationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.step.stupid.social.network.util.ConstantUtil.*;

@Component
@RequiredArgsConstructor
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final CookieOperation cookieOperation;
    private final SerializationHelper<OAuth2AuthorizationRequest, Cookie> serializationHelper;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Optional<Cookie> cookie = cookieOperation.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);

        return cookie
                .map(optionalCookie -> serializationHelper.deserialize(optionalCookie, OAuth2AuthorizationRequest.class))
                .orElseThrow(() -> new AuthenticationProviderException("Authorization request failed"));
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest oAuthRequest, HttpServletRequest request, HttpServletResponse response) {
        if (oAuthRequest == null) {
            this.removeAuthorizationRequestCookie(request, response);
            throw new AuthenticationProviderException("Authorization request is null");
        }
        String redirectUri = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        String oAuthSerializedObject = serializationHelper.serialize(oAuthRequest);

        cookieOperation.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, oAuthSerializedObject, AUTHORIZATION_COOKIE_EXPIRE_SECONDS);

        if (redirectUri != null) {
            cookieOperation.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri, COOKIE_EXPIRE_SECONDS);
        } else {
            cookieOperation.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, "/success", COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookie(HttpServletRequest request, HttpServletResponse response) {
        cookieOperation.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        cookieOperation.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
