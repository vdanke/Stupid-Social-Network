package com.step.stupid.social.network.dto.user.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLoginResponse {

    private LocalDateTime tokenCreationTime;
    private String token;
}
