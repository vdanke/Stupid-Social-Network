package com.step.stupid.social.network.controller;

import com.step.stupid.social.network.dto.user.request.UserLoginRequest;
import com.step.stupid.social.network.dto.user.request.UserRegistrationRequest;
import com.step.stupid.social.network.dto.user.response.UserLoginResponse;
import com.step.stupid.social.network.dto.user.response.UserRegistrationResponse;
import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.model.UserDetailsImpl;
import com.step.stupid.social.network.service.UserService;
import com.step.stupid.social.network.service.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping(
            path = "/registration",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserRegistrationResponse registration(
            @Valid @RequestBody UserRegistrationRequest registrationRequest
    ) {
        return userService.save(registrationRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = "Bearer " + tokenProvider.createToken(authentication);

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setId(userDetails.getId().toString());
        userLoginResponse.setUsername(userDetails.getUsername());
        userLoginResponse.setToken(token);

        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }
}
