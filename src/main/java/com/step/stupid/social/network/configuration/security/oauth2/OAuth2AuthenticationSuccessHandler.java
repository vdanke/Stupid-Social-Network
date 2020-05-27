package com.step.stupid.social.network.configuration.security.oauth2;

import com.step.stupid.social.network.configuration.security.user.OAuth2UserInfo;
import com.step.stupid.social.network.configuration.security.user.OAuth2UserInfoFactory;
import com.step.stupid.social.network.exception.OAuth2AuthorizationProcessingException;
import com.step.stupid.social.network.model.Role;
import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.repository.UserRepository;
import com.step.stupid.social.network.service.CookieOperation;
import com.step.stupid.social.network.service.SerializationHelper;
import com.step.stupid.social.network.service.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.step.stupid.social.network.util.ConstantUtil.*;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository requestRepository;
    private final CookieOperation cookieOperation;
    private final SerializationHelper<OAuth2AuthorizationRequest, Cookie> serializationHelper;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        DefaultOidcUser oAuth2User = (DefaultOidcUser) authentication.getPrincipal();

        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = cookieOperation
                .getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> serializationHelper.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElseThrow(() -> new OAuth2AuthorizationProcessingException("Authorization is empty"));

        Map<String, Object> googleAttributes = oAuth2AuthorizationRequest.getAttributes();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = (String) googleAttributes.get(PROVIDER_ID);

        OAuth2UserInfo oAuth2ConcreteProviderUser = OAuth2UserInfoFactory.getUserInfoByConcreteProvider(providerId, attributes);

        User user = getUserIfExists(oAuth2ConcreteProviderUser.getEmail())
                .map(existUser -> updateOldOAuth2User(oAuth2ConcreteProviderUser, providerId, existUser))
                .orElseGet(() -> registerOAuth2User(oAuth2ConcreteProviderUser, providerId));

        String token = tokenProvider.createToken(user);

        String redirectUrl = cookieOperation.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("/success");

        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost:8888")
                .path("/api/v1/auth" + redirectUrl)
                .queryParam("token", token)
                .build()
                .toUriString();
    }

    private User registerOAuth2User(OAuth2UserInfo oAuth2ConcreteProviderUser, String providerId) {
        User user = new User();

        user.setUsername(oAuth2ConcreteProviderUser.getEmail());
        user.setAuthorities(Collections.singleton(Role.ROLE_USER));
        user.setPassword(UUID.randomUUID().toString());
        user.setIsEnabled(true);
        user.setProvider(providerId);
        user.setGender(oAuth2ConcreteProviderUser.getGender());
        user.setImageUrl(oAuth2ConcreteProviderUser.getImageUrl());
        user.setFullName(oAuth2ConcreteProviderUser.getName());
        user.setLocale(oAuth2ConcreteProviderUser.getLocale());

        return user;
    }

    private User updateOldOAuth2User(OAuth2UserInfo oAuth2ConcreteProviderUser, String providerId, User existingUser) {
        existingUser.setFullName(oAuth2ConcreteProviderUser.getName());
        existingUser.setProvider(providerId);
        existingUser.setImageUrl(oAuth2ConcreteProviderUser.getImageUrl());
        existingUser.setGender(oAuth2ConcreteProviderUser.getGender());
        existingUser.setLocale(oAuth2ConcreteProviderUser.getLocale());

        return existingUser;
    }

    private Optional<User> getUserIfExists(String username) {
        return userRepository.findByUsername(username);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        requestRepository.removeAuthorizationRequestCookie(request, response);
    }
}
