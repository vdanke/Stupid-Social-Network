package com.step.stupid.social.network.configuration.security.user;

import com.step.stupid.social.network.configuration.security.AuthProvider;
import com.step.stupid.social.network.exception.UnknownProviderException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getUserInfoByConcreteProvider(String providerId, Map<String, Object> attributes) {
        if (providerId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        if (providerId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        }
        if (providerId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        }
        throw new UnknownProviderException(String.format("Provider %s not supported yet", providerId));
    }
}
