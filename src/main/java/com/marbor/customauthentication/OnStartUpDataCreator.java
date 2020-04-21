package com.marbor.customauthentication;

import com.marbor.customauthentication.domain.Authority;
import com.marbor.customauthentication.domain.User;
import com.marbor.customauthentication.repositories.AuthorityRepository;
import com.marbor.customauthentication.repositories.UserRepository;
import com.marbor.customauthentication.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OnStartUpDataCreator implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) {
        final Authority userAuthority = new Authority(AuthoritiesConstants.USER);
        authorityRepository.save(userAuthority);

        final var user = User.builder()
                .login("john")
                // password encrypted with bcrypt
                .password("$2a$10$DK0JxdpB5f9xMi1so/YkkeKOkWhO/alB01bIAYYjEkO4sL0FSpXCe")
                .firstName("John")
                .lastName("Smith")
                .createdDate(Instant.now())
                .activated(true)
                .authorities(Set.of(userAuthority))
                .build();

        userRepository.save(user);
    }
}
