package com.github.aleksannder.zavodzastatistiku.config.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class JwtAuthResolverConfig {

    //TODO Get via @Value from applciation.properties
    // And setup SSO
    @Value("${security.sso.enabled:false}")
    private boolean ssoEnabled;

    @Value("${security.sso.issuer}")
    private String ssoIssuer;

    @Value("${security.sso.jwks-uri}")
    private String ssoJwksUri;

    @Value("${security.jwt.local-issuer:zzs-local}")
    private String localIssuer;

    @Value("${security.jwt.secret}")
    private String secret;

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authManagerResolver() {
        Map<String, AuthenticationManager> managers = new HashMap<>();

        var localDecoder = NimbusJwtDecoder
                .withSecretKey(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")).build();
        managers.put(localIssuer, jwtManager(localDecoder, new LocalRoleConverter()));

        if (ssoEnabled) {
            JwtDecoder kc = (ssoJwksUri != null && !ssoJwksUri.isBlank())
                    ? NimbusJwtDecoder.withJwkSetUri(ssoJwksUri).build()
                    : NimbusJwtDecoder.withIssuerLocation(ssoIssuer).build();
            managers.put(ssoIssuer, jwtManager(kc, new KeycloakRoleConverter()));
        }

        return request -> {
            String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (auth != null && auth.startsWith("Bearer ")) {
                String iss = peekIssuer(auth.substring(7));
                var am = managers.get(iss);
                if (am != null) return am;
            }
            return managers.get(localIssuer);
        };
    }

    private AuthenticationManager jwtManager(JwtDecoder decoder,
                                             Converter<Jwt, ? extends AbstractAuthenticationToken> conv) {
        var provider = new JwtAuthenticationProvider(decoder);
        provider.setJwtAuthenticationConverter(conv);
        return provider::authenticate;
    }

    static class KeycloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {
            var roles = Optional.ofNullable((Map<String, Object>) jwt.getClaims().get("realm_access"))
                    .map(m -> (Collection<String>) m.get("roles")).orElse(List.of());
            var auth = roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList();
            return new JwtAuthenticationToken(jwt, auth, jwt.getSubject());
        }
    }

    static class LocalRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        @Override public AbstractAuthenticationToken convert(Jwt jwt) {
            var roles = Optional.ofNullable((Collection<String>) jwt.getClaims().get("roles")).orElse(List.of());
            var auth = roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList();
            return new JwtAuthenticationToken(jwt, auth, jwt.getSubject());
        }
    }

    private static String peekIssuer(String token) {
        try {
            String[] p = token.split("\\.");
            var json = new String(Base64.getUrlDecoder().decode(p[1]), StandardCharsets.UTF_8);
            return new ObjectMapper().readTree(json).path("iss").asText(null);
        } catch (Exception e) {
            return null;
        }
    }
}
