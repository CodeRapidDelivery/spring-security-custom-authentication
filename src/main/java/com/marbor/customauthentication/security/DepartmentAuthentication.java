package com.marbor.customauthentication.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class DepartmentAuthentication extends UsernamePasswordAuthenticationToken {
    private final String department;

    public DepartmentAuthentication(Object principal, Object credentials, String department) {
        super(principal, credentials);
        this.department = department;
    }

    public DepartmentAuthentication(String department, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(principal, null, authorities);
        this.department = department;
    }
}
