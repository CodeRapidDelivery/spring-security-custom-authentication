package com.marbor.customauthentication.security;

import com.marbor.customauthentication.resources.Routes;
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

    public DepartmentAuthFilter(AuthenticationManager authenticationManager) {
        this.setAuthenticationManager(authenticationManager);
        this.setFilterProcessesUrl(Routes.LOGIN_ROUTE);
        this.setAuthenticationSuccessHandler(DepartmentAuthFilter.getCustomSuccessHandler());

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String department = ofNullable(request.getParameter("department")).orElse("");
        final String username = ofNullable(request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY)).orElse("");
        final String password = ofNullable(request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY)).orElse("");

        CustomAuthentication customAuthenticationRequest = new CustomAuthentication(username, password, department);

        setDetails(request, customAuthenticationRequest);

        return this.getAuthenticationManager().authenticate(customAuthenticationRequest);
    }

    private static AuthenticationSuccessHandler getCustomSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            final String name = SecurityContextHolder.getContext().getAuthentication().getName();

            response.getOutputStream().print(name);
        };
    }
}
