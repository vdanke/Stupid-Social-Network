package com.step.stupid.social.network.model;

import com.step.stupid.social.network.service.validation.EmailConstraint;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class User extends BaseUser {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    private int old;

    @Column(name = "enabled")
    private Boolean isEnabled;

    @Column(name = "confirm_code")
    private String confirmCode;

    private String provider;

    private String fullName;

    private String imageUrl;

    private String gender;

    private String locale;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<GrantedAuthority> authorities;

    @Transient
    @OurAutowired
    private Role role;
}
