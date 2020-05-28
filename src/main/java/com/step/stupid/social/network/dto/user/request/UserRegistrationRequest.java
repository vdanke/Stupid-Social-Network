package com.step.stupid.social.network.dto.user.request;

import com.step.stupid.social.network.service.validation.EmailConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    @EmailConstraint(min = 20, max = 200, message = "Your email is not valid")
    private String username;
    @Size(min = 5, max = 64, message = "Too short or too long password")
    private String password;
    private int age;
}
