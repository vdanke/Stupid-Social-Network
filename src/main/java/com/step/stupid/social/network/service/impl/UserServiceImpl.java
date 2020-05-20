package com.step.stupid.social.network.service.impl;

import com.step.stupid.social.network.dto.user.request.UserRegistrationRequest;
import com.step.stupid.social.network.dto.user.request.UserUpdateRequest;
import com.step.stupid.social.network.dto.user.response.UserRegistrationResponse;
import com.step.stupid.social.network.dto.user.response.UserUpdateResponse;
import com.step.stupid.social.network.exception.NotFoundException;
import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.repository.UserRepository;
import com.step.stupid.social.network.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public UserRegistrationResponse save(UserRegistrationRequest registrationRequest) {
        User user = User.builder()
                .username(registrationRequest.getUsername())
                .password(registrationRequest.getPassword())
                .build();

        User afterSaving = userRepository.save(user);

        return UserRegistrationResponse.builder()
                .id(afterSaving.getId().toString())
                .username(afterSaving.getUsername())
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public UserUpdateResponse update(UserUpdateRequest userUpdateRequest) {
        UUID userId = UUID.fromString(userUpdateRequest.getId());

        User updateUser = User.builder()
                .id(userId)
                .username(userUpdateRequest.getUsername())
                .password(userUpdateRequest.getPassword())
                .build();

        User afterSaving = userRepository.save(updateUser);

        return UserUpdateResponse.builder()
                .id(afterSaving.getId().toString())
                .username(afterSaving.getUsername())
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<?> delete(String id) {
        UUID userId = UUID.fromString(id);

        User userIfExists = getUserIfExists(userId);

        userRepository.delete(userIfExists);

        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    private User getUserIfExists(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with ID %s not found", id.toString())));
    }
}
