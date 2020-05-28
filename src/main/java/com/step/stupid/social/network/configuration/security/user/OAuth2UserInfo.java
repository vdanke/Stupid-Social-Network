package com.step.stupid.social.network.configuration.security.user;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected final Map<String, Object> attributes;
    protected final String providerId;

    public OAuth2UserInfo(Map<String, Object> attributes, String providerId) {
        this.attributes = attributes;
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getGender();
    public abstract String getLocale();
    public abstract String getImageUrl();
}
