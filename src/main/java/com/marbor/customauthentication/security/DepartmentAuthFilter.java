package com.marbor.customauthentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marbor.customauthentication.resources.Routes;
import com.marbor.customauthentication.resources.dto.UserDto;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Optional.ofNullable;

public class DepartmentAuthFilter extends UsernamePasswordAuthenticationFilter {

    public DepartmentAuthFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.setAuthenticationManager(authenticationManager);
        this.setFilterProcessesUrl(Routes.LOGIN_ROUTE);
        this.setAuthenticationSuccessHandler(DepartmentAuthFilter.getCustomSuccessHandler(objectMapper));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String department = ofNullable(request.getParameter("department")).orElse("");
        final String username = ofNullable(request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY)).orElse("");
        final String password = ofNullable(request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY)).orElse("");

        DepartmentAuthentication departmentAuthenticationRequest = new DepartmentAuthentication(username, password, department);

        setDetails(request, departmentAuthenticationRequest);

        return this.getAuthenticationManager().authenticate(departmentAuthenticationRequest);
    }

    private static AuthenticationSuccessHandler getCustomSuccessHandler(ObjectMapper objectMapper) {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            final String name = SecurityContextHolder.getContext().getAuthentication().getName();
            final String department = ((DepartmentAuthentication) SecurityContextHolder.getContext().getAuthentication()).getDepartment();

            response.getOutputStream().print(objectMapper.writeValueAsString(new UserDto(name, department)));
        };
    }
}
