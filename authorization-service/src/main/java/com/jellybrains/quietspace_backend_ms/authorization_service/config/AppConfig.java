package com.jellybrains.quietspace_backend_ms.authorization_service.config;

import com.jellybrains.quietspace_backend_ms.authorization_service.entity.UserRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return (jwt) -> {
//            UserRepresentation user = getUserRepresentationFromJwt(); // TODO: implement method

            UserRepresentation user = UserRepresentation.builder()
                    .role("USER")
                    .username("guest")
                    .password("guest")
                    .id(UUID.randomUUID())
                    .email("guest@guest.com")
                    .build();

            List<GrantedAuthority> authorityList = Arrays.stream(user.getRole().split(","))
                    .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
                    .collect(Collectors.toList());

            return new User(user.getEmail(),user.getPassword(),authorityList);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));  //set access from all domains
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Headers"));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
