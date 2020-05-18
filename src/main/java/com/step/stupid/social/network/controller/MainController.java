package com.step.stupid.social.network.controller;

import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/main")
@RequiredArgsConstructor
public class MainController {

    private final UserRepository userRepository;

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(name = "id") String id) {
        UUID userId = UUID.fromString(id);

        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wrong id"));
    }
}
