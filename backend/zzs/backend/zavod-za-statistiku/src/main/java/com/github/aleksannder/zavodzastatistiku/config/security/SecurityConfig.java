package com.github.aleksannder.zavodzastatistiku.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

@EnableWebSecurity
@Configuration
@EnableScheduling
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
                        .requestMatchers("/api/citizen/**").hasAnyRole("CITIZEN", "ADMIN", "ANALYST")
                        .requestMatchers("/api/analyst/**").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwt.jwtAuthenticationConverter(this::toAuth)));
        return http.build();
    }

    private AbstractAuthenticationToken toAuth(Jwt jwt) {
        var auths = new ArrayList<GrantedAuthority>();

        var realmRoles = Optional.ofNullable((Map<String, Object>) jwt.getClaims().get("realm_access"))
                .map(m -> (Collection<String>) m.get("roles")).orElse(Collections.emptyList());
        realmRoles.forEach(r -> auths.add(new SimpleGrantedAuthority("ROLE" + r)));

        var resource = Optional.ofNullable((Map<String, Object>) jwt.getClaims().get("resource_access"))
                .orElse(Collections.emptyMap());
        resource.values().forEach(v -> {
            var roles = (Collection<String>) ((Map<String, Object>) v).getOrDefault("roles", Collections.emptyList());
            roles.forEach(r -> auths.add(new SimpleGrantedAuthority("ROLE" + r)));
        });

        return new JwtAuthenticationToken(jwt, auths, jwt.getSubject());
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}
