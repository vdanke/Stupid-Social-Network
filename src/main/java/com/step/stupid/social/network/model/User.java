package com.step.stupid.social.network.model;

import com.step.stupid.social.network.service.validation.EmailConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @EmailConstraint(min = 20, max = 200, message = "Organization email is not valid")
    @Column(name = "username", unique = true)
    private String username;

    @Size(min = 5, max = 1024, message = "Password is nov fitted, check max min length")
    @Column(name = "password", length = 1024)
    private String password;

    @Column(name = "enabled")
    private Boolean isEnabled;

    @Column(name = "confirm_code")
    private String confirmCode;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<GrantedAuthority> authorities;

    @Transient
    @OurAutowired
    private Role role;
}
