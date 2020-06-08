package com.step.stupid.social.network.service.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.step.stupid.social.network.model.User;
import org.springframework.util.StringUtils;

import java.util.stream.Stream;

public class UserFilter extends Filter<User> {

    private String fullName;
    private String gender;

    /*
    {
        "active":false,
        "fullName":"name",
        "gender":"gender"
    }
     */

    @JsonCreator
    protected UserFilter(@JsonProperty("active") boolean isActive,
                         @JsonProperty("fullName") String fullName,
                         @JsonProperty("gender") String gender) {
        super(isActive);
        this.fullName = fullName;
        this.gender = gender;
    }

    @Override
    public Stream<User> filter(Stream<User> userStream) {
        if (!fullName.isEmpty()) {
            userStream = userStream.filter(user -> user.getFullName().contains(fullName));
        }
        if (!StringUtils.isEmpty(fullName)) {
            userStream = userStream.filter(user -> user.getGender().equalsIgnoreCase(gender));
        }
        return userStream;
    }
}
