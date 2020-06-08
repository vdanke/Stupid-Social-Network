package com.step.stupid.social.network.model;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ROLE_ADMIN("admin"),
    ROLE_USER("user");

    private String role;

    Role(String role){
        this.role = role;
    }

    @JsonValue
    public String getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
