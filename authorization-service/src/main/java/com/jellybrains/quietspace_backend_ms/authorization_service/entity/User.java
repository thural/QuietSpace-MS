package com.jellybrains.quietspace_backend_ms.authorization_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jellybrains.quietspace_backend_ms.authorization_service.utils.enums.StatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails, Principal {

    @NotNull
    @NotBlank
    @Column(length = 32, unique = true)
    private String username;

    @NotNull
    @NotBlank
    @Column(length = 32, unique = true)
    private String email;

    @NotNull
    @NotBlank
    @JsonIgnore
    private String password;


    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_followings",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followings_id")
    )
    private List<User> followings = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "followings")
    private List<User> followers = new ArrayList<>();


    @JsonIgnore
    private String firstname;
    @JsonIgnore
    private String lastname;
    @JsonIgnore
    private OffsetDateTime dateOfBirth;
    @JsonIgnore
    private boolean accountLocked;
    @JsonIgnore
    private boolean enabled;
    @JsonIgnore
    private StatusType statusType;

    @ManyToMany(fetch = EAGER)
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
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
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String fullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String getName() {
        return username;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    @PrePersist
    void initAccount() {
        setEnabled(false);
        setAccountLocked(false);
        setStatusType(StatusType.OFFLINE);
    }

}