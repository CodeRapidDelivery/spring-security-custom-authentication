package com.marbor.customauthentication.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final DomainUserDetailsService domainUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var customAuthentication = (CustomAuthentication) authentication;
        if (!"IT".equals(customAuthentication.getDepartment())) {
            throw new NotAllowedDepartmentException();
        }

        final UserDetails userDetails = domainUserDetailsService.loadUserByUsername(customAuthentication.getPrincipal().toString());
        if (!passwordEncoder.matches(customAuthentication.getCredentials().toString(), userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        return new CustomAuthentication(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.equals(authentication);
    }
}

class NotAllowedDepartmentException extends AuthenticationException {

    public NotAllowedDepartmentException() {
        super("Not allowed department");
    }
}