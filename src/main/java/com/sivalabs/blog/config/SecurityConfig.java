package com.sivalabs.blog.config;

import com.sivalabs.blog.users.TokenHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class SecurityConfig {

    @Bean
    JwtFilter jwtFilter(TokenHelper tokenHelper, UserDetailsService uds) {
        return new JwtFilter(tokenHelper, uds);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService uds, PasswordEncoder encoder) {
        var authProvider = new DaoAuthenticationProvider(uds);
        authProvider.setPasswordEncoder(encoder);

        return new ProviderManager(authProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
