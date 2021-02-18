package com.marbor.customauthentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marbor.customauthentication.resources.Routes;
import com.marbor.customauthentication.resources.dto.UserDto;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

public class DepartmentAuthFilter extends AbstractAuthenticationProcessingFilter {

    public DepartmentAuthFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, JwtTokenOperations jwtTokenOperations) {
        super(Routes.LOGIN_ROUTE);
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(DepartmentAuthFilter.getCustomSuccessHandler(objectMapper, jwtTokenOperations));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String department = request.getParameter("department");
        final String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        final String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);

        if (department == null || username == null || password == null) {
            return null;
        }

        final var departmentAuthenticationRequest = new DepartmentAuthentication(username, password, department);

        return this.getAuthenticationManager().authenticate(departmentAuthenticationRequest);
    }

    private static AuthenticationSuccessHandler getCustomSuccessHandler(ObjectMapper objectMapper, JwtTokenOperations jwtTokenOperations) {
        return (request, response, authentication) -> {

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            final var name = SecurityContextHolder.getContext().getAuthentication().getName();

            final String token = jwtTokenOperations.createToken(name, authentication.getAuthorities());
            response.getOutputStream().print(token);
        };
    }
}
