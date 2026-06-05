package com.rentauto.paymentprovider.config;

import com.rentauto.paymentprovider.domain.User;
import com.rentauto.paymentprovider.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsServiceConfig {

    private final UserRepository userRepository;

    public UserDetailsServiceConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
            return user;
        };
    }
}
