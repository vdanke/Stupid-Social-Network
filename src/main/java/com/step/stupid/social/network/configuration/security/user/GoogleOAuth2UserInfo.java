package com.step.stupid.social.network.configuration.security.user;

import java.util.Map;

import static com.step.stupid.social.network.util.ConstantUtil.*;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes, String providerId) {
        super(attributes, providerId);
    }

    @Override
    public String getId() {
        return (String) attributes.get(PROVIDER_ID);
    }

    @Override
    public String getName() {
        return (String) attributes.get(PROVIDER_NAME);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(PROVIDER_EMAIL);
    }

    @Override
    public String getGender() {
        return (String) attributes.get(PROVIDER_GENDER);
    }

    @Override
    public String getLocale() {
        return (String) attributes.get(PROVIDER_LOCALE);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(PROVIDER_PICTURE);
    }
}
