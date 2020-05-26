package com.step.stupid.social.network.controller;

import com.step.stupid.social.network.dto.user.request.UserUpdateRequest;
import com.step.stupid.social.network.dto.user.response.UserUpdateResponse;
import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.model.UserDetailsImpl;
import com.step.stupid.social.network.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") String id) {
        return userService.delete(id);
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
    public UserUpdateResponse updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(userUpdateRequest);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/profile")
    public ResponseEntity<?> getUserInSecuritySessionProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }
}
