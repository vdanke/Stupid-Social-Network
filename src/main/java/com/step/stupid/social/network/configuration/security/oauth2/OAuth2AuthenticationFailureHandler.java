package com.step.stupid.social.network.configuration.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository requestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirect = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost:8888")
                .path("/auth/error")
                .queryParam("message", exception.getLocalizedMessage())
                .build()
                .toUriString();

        requestRepository.removeAuthorizationRequestCookie(request, response);

        getRedirectStrategy().sendRedirect(request, response, redirect);
    }
}
