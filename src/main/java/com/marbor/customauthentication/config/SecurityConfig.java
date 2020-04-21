package com.marbor.customauthentication.config;

import com.marbor.customauthentication.security.CustomAuthenticationProvider;
import com.marbor.customauthentication.security.DepartmentAuthFilter;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .csrf()
            // for production ready app you wanna enable csrf support
            .disable()
            .addFilterAfter(corsFilter, CsrfFilter.class)
            .addFilterBefore(new DepartmentAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(((request, response, e) -> response.setStatus(401)))
            .accessDeniedHandler(((request, response, e) -> response.setStatus(403)))
        .and()
            .formLogin()
            .loginProcessingUrl(LOGIN_ROUTE)
            .successHandler(((request, response, authentication) -> response.setStatus(200)))
            .failureHandler((request, response, e) -> response.sendError(401, "Authentication failed"))
            .permitAll()
        .and()
            .logout()
            .logoutUrl(LOGOUT_ROUTE)
            .logoutSuccessHandler(getLogoutHandler())
        .and()
            .authorizeRequests()
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
        auth.authenticationProvider(customAuthenticationProvider);
    }

    private LogoutSuccessHandler getLogoutHandler() {
        return (request, response, authentication) -> log.debug("{} logged out", authentication.getName());
    }
}

@Configuration
class AdditionalConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
