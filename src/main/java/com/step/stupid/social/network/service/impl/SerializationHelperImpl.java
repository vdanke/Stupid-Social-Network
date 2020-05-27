package com.step.stupid.social.network.service.impl;

import com.step.stupid.social.network.service.SerializationHelper;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import java.util.Base64;

@Service
public class SerializationHelperImpl implements SerializationHelper<OAuth2AuthorizationRequest, Cookie> {

    @Override
    public String serialize(OAuth2AuthorizationRequest oAuth2AuthorizationRequest) {
        byte[] serializedAuthorizationRequest = SerializationUtils.serialize(oAuth2AuthorizationRequest);

        return Base64.getUrlEncoder().encodeToString(serializedAuthorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest deserialize(Cookie cookie, Class<OAuth2AuthorizationRequest> cls) {
        String toDeserialize = cookie.getValue();

        byte[] serializedAuthorizationRequest = Base64.getUrlDecoder().decode(toDeserialize);

        Object oAuth2AuthorizationRequest = SerializationUtils.deserialize(serializedAuthorizationRequest);

        return cls.cast(oAuth2AuthorizationRequest);
    }
}
