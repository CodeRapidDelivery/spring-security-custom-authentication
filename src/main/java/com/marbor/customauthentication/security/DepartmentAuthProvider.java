package com.marbor.customauthentication.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentAuthProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final DomainUserDetailsService domainUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final var customAuthentication = (DepartmentAuthentication) authentication;
        if (!"IT".equals(customAuthentication.getDepartment())) {
            throw new NotAllowedDepartmentException();
        }

        final var userDetails = domainUserDetailsService.loadUserByUsername(customAuthentication.getPrincipal().toString());
        if (!passwordEncoder.matches(customAuthentication.getCredentials().toString(), userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        return new DepartmentAuthentication(
                customAuthentication.getDepartment(),
                userDetails,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DepartmentAuthentication.class.equals(authentication);
    }
}

