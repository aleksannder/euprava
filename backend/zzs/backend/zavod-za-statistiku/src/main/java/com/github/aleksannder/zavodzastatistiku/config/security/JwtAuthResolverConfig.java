package com.github.aleksannder.zavodzastatistiku.config.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Configuration
public class JwtAuthResolverConfig {

    //TODO Get via @Value from applciation.properties
    // And setup SSO
    private static final boolean SSO_ENABLED = false;
    private static final String SSO_ISSUER = "http://localhost:8081/realms/gov-suite";
    private static final String LOCAL_ISSUER = "zzs-local";
    private static final byte[] LOCAL_SECRET = "to-change-in-future".getBytes();
    private static final String SSO_JWKS_URI = "http://localhost:8081/realms/gov-suite/protocol/openid-connect/certs";

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authManagerResolver() {
        Map<String, AuthenticationManager> managers = new HashMap<>();

        managers.put(LOCAL_ISSUER, jwtManager(localDecoder(), new LocalRoleConverter()));

        if (SSO_ENABLED) {
            JwtDecoder ssoDecoder = !SSO_JWKS_URI.isBlank()
                    ? NimbusJwtDecoder.withJwkSetUri(SSO_JWKS_URI).build()  // ne radi discovery na startu
                    : NimbusJwtDecoder.withIssuerLocation(SSO_JWKS_URI).build(); // radi discovery
            managers.put(SSO_ISSUER, jwtManager(ssoDecoder, new KeycloakRoleConverter()));
        } else {
            System.out.println("SSO disabled");
        }

        return request -> {
            String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);
                String issuer = JwtPeekHelper.peekIssuer(token);
                var am = managers.get(issuer);
                if (am != null) return am;
            }

            return managers.get(LOCAL_ISSUER);
        };
    }

    private AuthenticationManager jwtManager(JwtDecoder decoder,
                                             Converter<Jwt, ? extends AbstractAuthenticationToken> conv) {
        var provider = new JwtAuthenticationProvider(decoder);
        provider.setJwtAuthenticationConverter(conv);
        return provider::authenticate;
    }

    private JwtDecoder nimbusDecoderByIssuer() {
        return NimbusJwtDecoder.withIssuerLocation(JwtAuthResolverConfig.SSO_ISSUER).build();
    }

    private JwtDecoder localDecoder() {
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(LOCAL_SECRET, "HmacSHA256")).build();
    }

    static class KeycloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {
            Collection<String> roles = Optional.ofNullable((Map<String, Object>) jwt.getClaims().get("realm_access"))
                    .map(m -> (Collection<String>) m.get("roles"))
                    .orElse(List.of());
            var auth = roles.stream().map(r -> "ROLE_" + r).map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).toList();
            return new JwtAuthenticationToken(jwt, auth, jwt.getSubject());
        }
    }

    static class LocalRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        @Override
        public AbstractAuthenticationToken convert(Jwt source) {
            Collection<String> roles = Optional.ofNullable((Collection<String>) source.getClaims().get("roles")).orElse(List.of());
            var auth = roles.stream().map(r -> "ROLE_" + r).map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).toList();
            return new JwtAuthenticationToken(source, auth, source.getSubject());
        }
    }

    static class JwtPeekHelper {
        private static final ObjectMapper M = new ObjectMapper();
        static String peekIssuer(String token) {
            try {
                String[] parts = token.split("\\.");
                if (parts.length < 2) return null;
                String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
                Map<String, Object> map = M.readValue(payloadJson, new TypeReference<>() {});
                Object issuer = map.get("iss");
                return issuer != null ? issuer.toString() : null;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
