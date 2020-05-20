package com.step.stupid.social.network.dto.user.request;

import com.step.stupid.social.network.service.validation.EmailConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @NotBlank
    private String id;
    @EmailConstraint
    private String username;
    @Size(min = 5, max = 64, message = "Password is not valid")
    private String password;
}
