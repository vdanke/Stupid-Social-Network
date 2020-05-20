package com.step.stupid.social.network.dto.user.response;

import lombok.Data;

@Data
public class UserLoginResponse {

    private String id;
    private String username;
    private String token;
}
