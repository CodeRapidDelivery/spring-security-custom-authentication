package com.marbor.customauthentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marbor.customauthentication.security.DepartmentAuthFilter;
import com.marbor.customauthentication.security.DepartmentAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CorsFilter;

import static com.marbor.customauthentication.resources.Routes.LOGIN_ROUTE;
import static com.marbor.customauthentication.resources.Routes.LOGOUT_ROUTE;
import static javax.servlet.http.HttpServletResponse.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;
    private final DepartmentAuthenticationProvider departmentAuthenticationProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .csrf()
            // for production ready app you wanna enable csrf support
            .disable()
            .addFilterAfter(corsFilter, CsrfFilter.class)
            .addFilterBefore(new DepartmentAuthFilter(authenticationManager(), objectMapper), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(((request, response, e) -> response.setStatus(SC_UNAUTHORIZED)))
            .accessDeniedHandler(((request, response, e) -> response.setStatus(SC_FORBIDDEN)))
        .and()
            .logout()
            .logoutUrl(LOGOUT_ROUTE)
            .logoutSuccessHandler(getLogoutSuccessHandler())
        .and()
            .authorizeRequests()
            .antMatchers(LOGIN_ROUTE).permitAll()
            .anyRequest().authenticated();
        //@formatter:on
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/webjars/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/h2-console/**")
                .antMatchers("/**/*.{js,html,css}");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(departmentAuthenticationProvider);
    }

    private LogoutSuccessHandler getLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            log.debug("{} logged out", authentication.getName());
            response.setStatus(SC_OK);
        };
    }
}

@Configuration
class AdditionalConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
