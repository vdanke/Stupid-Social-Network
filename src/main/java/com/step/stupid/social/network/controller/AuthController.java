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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
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

    @PostMapping(path = "/login",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        final String username = loginRequest.getUsername();
        final String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = "Bearer " + tokenProvider.createToken(authentication);

        UserLoginResponse userLoginResponse = new UserLoginResponse();

        userLoginResponse.setTokenCreationTime(LocalDateTime.now());
        userLoginResponse.setToken(token);

        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }

    @GetMapping("/confirm/{confirmationCode}")
    public ResponseEntity<?> confirmAccount(@PathVariable(name = "confirmationCode") String code) {
        return userService.confirmExistUser(code);
    }

    @GetMapping("/success")
    public String getSuccessPage(@RequestParam(name = "token") String token) {
        return token;
    }

    @GetMapping("/error")
    public String getErrorPage(@RequestParam(name = "message") String message) {
        return message;
    }
}
