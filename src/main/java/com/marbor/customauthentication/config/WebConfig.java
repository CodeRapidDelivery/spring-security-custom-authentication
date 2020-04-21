package com.marbor.customauthentication.config;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.marbor.customauthentication.properties.WebProperties;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebConfig {
    private final WebProperties webProperties;

    @Bean
    public CorsFilter corsFilter() {
        final var source = new UrlBasedCorsConfigurationSource();
        final var config = webProperties.getCors();

        Optional.ofNullable(config.getAllowedOrigins())
                .filter(origins -> !origins.isEmpty())
                .ifPresent(origins -> source.registerCorsConfiguration("/**", config));

        return new CorsFilter(source);
    }
}
