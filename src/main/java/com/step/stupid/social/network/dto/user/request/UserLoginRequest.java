package com.step.stupid.social.network.dto.user.request;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonRootName(value = "login")
@JsonIgnoreProperties(value = {"id"}, ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserLoginRequest {

    @NotBlank
    @JsonProperty(value = "username")
    private String username;
    @NotBlank
    private String password;
    @JacksonInject
    private int id;

    /*
    {
        "login": {
            "username":"username",
            "password":"password
        }
    }
     */
}
