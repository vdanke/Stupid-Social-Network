package com.step.stupid.social.network.configuration.security.user;

import com.step.stupid.social.network.configuration.security.AuthProvider;
import com.step.stupid.social.network.exception.UnknownProviderException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getUserInfoByConcreteProvider(String providerId, Map<String, Object> attributes) {
        if (providerId.equalsIgnoreCase(AuthProvider.GOOGLE.name())) {
            return new GoogleOAuth2UserInfo(attributes, providerId);
        }
        if (providerId.equalsIgnoreCase(AuthProvider.FACEBOOK.name())) {
            return new FacebookOAuth2UserInfo(attributes, providerId);
        }
        if (providerId.equalsIgnoreCase(AuthProvider.GITHUB.name())) {
            return new GithubOAuth2UserInfo(attributes, providerId);
        }
        throw new UnknownProviderException(String.format("Provider %s not supported yet", providerId));
    }
}
