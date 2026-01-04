package com.example.ams.user;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

    ADMIN,
    SYSTEM_USER,
    WORKER,
    SIMPLE_EMPLOYEE,
    CUSTOMER;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
