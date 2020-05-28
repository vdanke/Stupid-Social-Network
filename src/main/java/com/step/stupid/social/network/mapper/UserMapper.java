package com.step.stupid.social.network.mapper;

import com.step.stupid.social.network.dto.user.request.UserRegistrationRequest;
import com.step.stupid.social.network.dto.user.response.UserRegistrationResponse;
import com.step.stupid.social.network.model.User;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = PasswordEncoder.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "old", expression = "java(request.getAge())"),
            @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))"),
            @Mapping(target = "username", expression = "java(request.getUsername())"),
    })
    User requestToUser(UserRegistrationRequest request, PasswordEncoder passwordEncoder);

    @Mappings({
            @Mapping(target = "id", expression = "java(user.getId().toString())"),
            @Mapping(target = "username", expression = "java(user.getUsername())")
    })
    UserRegistrationResponse userToResponse(User user);
}
