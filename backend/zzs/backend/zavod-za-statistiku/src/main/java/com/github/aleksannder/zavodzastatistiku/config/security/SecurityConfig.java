package com.github.aleksannder.zavodzastatistiku.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String CITIZEN_CLAIM = "https://egov.local/claims/citizen";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/api/public/**", "/api/auth/**").permitAll()
                        .requestMatchers("/api/citizen/**").hasAnyRole("CITIZEN", "ANALYST", "ADMIN")
                        .requestMatchers("/api/analyst/**").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(permissionsToAuthorities()))
                );
        return http.build();
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> permissionsToAuthorities() {
        return jwt -> {
            var auths = new ArrayList<GrantedAuthority>();
            var perms = (Collection<String>) jwt.getClaims().getOrDefault("permissions", List.of());

            for (String p : perms) {
                switch (p) {
                    case "citizen:access"  -> auths.add(new SimpleGrantedAuthority("ROLE_CITIZEN"));
                    case "zzs:analyst" -> auths.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                    case "zzs:admin" -> auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }
            }
            return new JwtAuthenticationToken(jwt, auths, jwt.getSubject());
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:4300"));
        cors.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cors.setAllowedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}
