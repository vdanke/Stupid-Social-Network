package com.step.stupid.social.network.configuration.security.user;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    public FacebookOAuth2UserInfo(Map<String, Object> attributes, String providerId) {
        super(attributes, providerId);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getLocale() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }
}
