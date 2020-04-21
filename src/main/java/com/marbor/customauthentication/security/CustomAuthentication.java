package com.marbor.customauthentication.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Getter
public class CustomAuthentication extends UsernamePasswordAuthenticationToken {
    private String department;

    public CustomAuthentication(Object principal, Object credentials, String department) {
        super(principal, credentials);
        this.department = department;
    }

    public CustomAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
