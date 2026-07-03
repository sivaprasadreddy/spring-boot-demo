package com.sivalabs.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {
    private static final String[] PUBLIC_RESOURCES = {
        "/", "/favicon.ico", "/actuator/**", "/error", "/swagger-ui/**", "/v3/api-docs/**",
    };

    private final TokenAuthenticationFilter authenticationFilter;

    WebSecurityConfig(TokenAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.securityMatcher("/api/**");
        http.csrf(CsrfConfigurer::disable);
        http.cors(CorsConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());
        http.exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class);

        http.authorizeHttpRequests(c -> c.requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .requestMatchers("/api/login")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/**")
                .permitAll()
                .anyRequest()
                .authenticated());

        return http.build();
    }
}
