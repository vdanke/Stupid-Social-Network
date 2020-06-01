package com.step.stupid.social.network.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class BaseUser {

    private String username;
    private String password;
}
