package com.marbor.customauthentication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marbor.customauthentication.security.DepartmentAuthFilter;
import com.marbor.customauthentication.security.DepartmentAuthProvider;
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

import static com.marbor.customauthentication.resources.Routes.LOGIN_ROUTE;
import static com.marbor.customauthentication.resources.Routes.LOGOUT_ROUTE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DepartmentAuthProvider departmentAuthProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .csrf().disable()
            .addFilterBefore(new DepartmentAuthFilter(authenticationManager(), objectMapper), UsernamePasswordAuthenticationFilter.class)
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
        auth.authenticationProvider(departmentAuthProvider);
    }

    private LogoutSuccessHandler getLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            log.debug("{} logged out", authentication.getName());
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
