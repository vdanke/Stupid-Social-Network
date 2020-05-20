package com.step.stupid.social.network.dto.user.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
