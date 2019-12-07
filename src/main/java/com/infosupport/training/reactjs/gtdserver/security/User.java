package com.infosupport.training.reactjs.gtdserver.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.requireNonNull;

@Builder
@Getter
@Table("USERS")
@With
public class User implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password; // Yes, this should be char[], but Springs UserDetail class models it as a String :(

    @JsonCreator
    User(@JsonProperty("id") final String id,
         @JsonProperty("username") final String username,
         @JsonProperty("password") final String password) {
        super();
        this.id = id;
        this.username = requireNonNull(username);
        this.password = requireNonNull(password);
    }

    @JsonIgnore
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}
