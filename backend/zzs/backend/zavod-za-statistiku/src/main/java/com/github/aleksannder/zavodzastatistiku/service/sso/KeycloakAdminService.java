package com.github.aleksannder.zavodzastatistiku.service.sso;

import com.github.aleksannder.zavodzastatistiku.dto.auth.UserRegisterRequestDto;
import com.github.aleksannder.zavodzastatistiku.dto.auth.UserRegisterResponseDto;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    @Value("${kc.server}")
    private String keycloakServer;

    @Value("${kc.realm}")
    private String keycloakRealm;

    @Value("${kc.admin.username}")
    private String adminUsername;

    @Value("${kc.admin.password}")
    private String adminPassword;

    private final WebClient webClient = WebClient.builder().build();

    private Mono<String> adminToken() {
        return webClient.post()
                .uri(keycloakServer + "/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("client_id", "admin-cli")
                        .with("grant_type", "password")
                        .with("username", adminUsername)
                        .with("password", adminPassword))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(m -> (String) m.get("access_token"));
    }

    public Mono<UserRegisterResponseDto> registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        return adminToken().flatMap(token ->

                webClient.post()
                        .uri(keycloakServer + "/admin/realms/{realm}/users", keycloakRealm)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of(
                                "email", userRegisterRequestDto.email().toLowerCase(),
                                "firstName", userRegisterRequestDto.firstName(),
                                "lastName", userRegisterRequestDto.lastName(),
                                "password", userRegisterRequestDto.password()))
                        .exchangeToMono(resp -> {
                            if (resp.statusCode().is2xxSuccessful()) {
                                String loc = resp.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION);
                                String userId = Objects.requireNonNull(loc).substring(loc.lastIndexOf('/') + 1);
                                return Mono.just(userId);
                            }
                            return resp.bodyToMono(String.class)
                                    .flatMap(b -> Mono.error(new RuntimeException("KC create user failed: " + b)));
                        })
                        .flatMap(userId -> {
                            if (userRegisterRequestDto.password() == null || userRegisterRequestDto.password().isBlank()) return Mono.just(userId);
                            return webClient.put()
                                    .uri(keycloakServer + "/admin/realms/{realm}/users/{id}/reset-password", keycloakRealm, userId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of(
                                            "type", "password", "value", userRegisterRequestDto.password(), "temporary", false
                                    ))
                                    .retrieve().toBodilessEntity().thenReturn(userId);
                        })
                        .flatMap(userId ->
                            webClient.get()
                                    .uri(keycloakServer + "/admin/realms/{realm}/roles/{role}", keycloakRealm, Role.CITIZEN)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                    .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                                    .flatMap(role ->
                                            webClient.post()
                                                    .uri(keycloakServer + "/admin/realms/{realm}/users/{id}/role-mappings/realm", keycloakRealm, userId)
                                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .bodyValue(List.of(Map.of("id", role.get("id"), "name", role.get("name"))))
                                                    .retrieve().toBodilessEntity().thenReturn(userId)
                                            )
                        )
                        .flatMap(userId ->
                                webClient.get()
                                        .uri(keycloakServer + "/admin/realms/{realm}/users/{id}", keycloakRealm, userId)
                                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                        .retrieve()
                                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                                        .map(u -> new UserRegisterResponseDto(
                                                (String) u.get("id"),
                                                (String) u.get("email"),
                                                true,
                                                true
                                        ))
                                )
                );
    }
}
