package com.step.stupid.social.network.service;

import com.step.stupid.social.network.dto.user.request.UserRegistrationRequest;
import com.step.stupid.social.network.dto.user.request.UserUpdateRequest;
import com.step.stupid.social.network.dto.user.response.UserRegistrationResponse;
import com.step.stupid.social.network.dto.user.response.UserUpdateResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.ResponseEntity;

public interface UserService {

//    @Procedure(procedureName = "User.getAll")
    UserRegistrationResponse save(UserRegistrationRequest registrationRequest);

    UserUpdateResponse update(UserUpdateRequest userUpdateRequest);

    ResponseEntity<?> delete(String id);

    ResponseEntity<?> confirmExistUser(String code);
}
