package com.step.stupid.social.network.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {

    private UUID id;
    private String username;
    private String password;
    private Boolean isEnabled;
    private Set<GrantedAuthority> authorities;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(UUID id, String username, String password, Set<GrantedAuthority> authorities, Boolean isEnabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isEnabled = isEnabled;
    }

    public static UserDetailsImpl create(User user) {
        return new UserDetailsImpl(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getAuthorities(),
            user.getIsEnabled()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public UUID getId() {
        return id;
    }
}
