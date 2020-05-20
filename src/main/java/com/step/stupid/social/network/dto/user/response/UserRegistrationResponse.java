package com.step.stupid.social.network.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserRegistrationResponse {

    private String id;
    private String username;
}
