package com.marbor.customauthentication.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenOperations jwtTokenOperations;

    public JwtTokenFilterConfigurer(JwtTokenOperations jwtTokenOperations) {
        this.jwtTokenOperations = jwtTokenOperations;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenOperations);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
