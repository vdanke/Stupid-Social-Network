package com.step.stupid.social.network.service.impl;

import com.step.stupid.social.network.dto.user.request.UserRegistrationRequest;
import com.step.stupid.social.network.dto.user.request.UserUpdateRequest;
import com.step.stupid.social.network.dto.user.response.UserRegistrationResponse;
import com.step.stupid.social.network.dto.user.response.UserUpdateResponse;
import com.step.stupid.social.network.exception.NotFoundException;
import com.step.stupid.social.network.mapper.UserMapper;
import com.step.stupid.social.network.model.User;
import com.step.stupid.social.network.repository.UserRepository;
import com.step.stupid.social.network.service.MailService;
import com.step.stupid.social.network.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Value("${server.host}")
    private String host;
    @Value("${server.port}")
    private int port;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public UserRegistrationResponse save(UserRegistrationRequest registrationRequest) {
        final String subject = "Welcome to our server";
        final String address = host + ":" + port;
        final String uniqueConfirmationCode = UUID.randomUUID().toString() + registrationRequest.getUsername();

        String text = String.format("Hello there, great to see you here. To confirm your email follow the link bellow \n" +
                "http://%s/api/v1/auth/confirm/%s", address, uniqueConfirmationCode);

        User user = userMapper.requestToUser(registrationRequest, passwordEncoder);

        User afterSaving = userRepository.save(user);

        mailService.send(registrationRequest.getUsername(), subject, text);

        return userMapper.userToResponse(afterSaving);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public UserUpdateResponse update(UserUpdateRequest userUpdateRequest) {
        UUID userId = UUID.fromString(userUpdateRequest.getId());

        User updateUser = new User();

//        User updateUser = User.builder()
//                .id(userId)
//                .username(userUpdateRequest.getUsername())
//                .password(userUpdateRequest.getPassword())
//                .build();

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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<?> confirmExistUser(String code) {
        User user = userRepository.findByConfirmCode(code)
                .orElseThrow(() -> new NotFoundException("Code is not correct, please try again"));

        user.setConfirmCode(null);
        user.setIsEnabled(true);

        User userAfterSaving = userRepository.save(user);

        UserUpdateResponse response = UserUpdateResponse.builder()
                .id(userAfterSaving.getId().toString())
                .username(userAfterSaving.getUsername())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private User getUserIfExists(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with ID %s not found", id.toString())));
    }
}
