package com.github.aleksannder.zavodzastatistiku.service.sso;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Auth0ManagementClient {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.m2m.client.id}")
    private String clientId;

    @Value("${auth0.m2m.client.secret}")
    private String clientSecret;

    @Value("${auth0.m2m.audience}")
    private String audience;

    private final Map<String, String> roleIdCache = new java.util.concurrent.ConcurrentHashMap<>();
    private Mono<String> managementToken() {
        return webClient.post()
                .uri("https://" + domain + "/oauth/token")
                .bodyValue(Map.of(
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "audience", audience,
                        "grant_type", "client_credentials"
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(m -> (String) m.get("access_token"));
    }

    public Mono<Map<String, Object>> createDbUser(String email, String password,
                                                  String firstName, String lastName,
                                                  String connection) {
        return managementToken().flatMap(token ->
                webClient.post()
                        .uri("https://" + domain + "/api/v2/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of(
                                "connection", connection, // e.g. "Username-Password-Authentication"
                                "email", email.toLowerCase(),
                                "password", password,
                                "email_verified", false,
                                "given_name", firstName,
                                "family_name", lastName
                        ))
                        .exchangeToMono(resp -> {
                            if (resp.statusCode().is2xxSuccessful()) {
                                return resp.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
                            }
                            return resp.bodyToMono(String.class).flatMap(b ->
                                    Mono.error(new RuntimeException("failed to create db user [" + resp.statusCode().value() + "]: " + b)));
                        })
        );
    }

    public Mono<Map<String, Object>> getUserById(String userId) {
        return managementToken().flatMap(token ->
                webClient.get()
                        .uri("https://" + domain + "/api/v2/users/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
        );
    }

    public Mono<Void> updateUserMetadata(String userId, Map<String, Object> appMetadata) {
        return managementToken().flatMap(token ->
                webClient.patch()
                        .uri("https://" + domain + "/api/v2/users/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("app_metadata", appMetadata))
                        .retrieve()
                        .toBodilessEntity()
                        .then()
        );
    }

    public Mono<String> getRoleIdByName(String roleName) {
        // cache to avoid repeated list calls
        String cached = roleIdCache.get(roleName);
        if (cached != null) return Mono.just(cached);

        return managementToken().flatMap(token ->
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host(domain)
                                .path("/api/v2/roles")
                                .queryParam("name_filter", roleName)
                                .queryParam("per_page", 50)
                                .build())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Map<String,Object>>>() {})
        ).flatMap(list -> {
            var match = list.stream()
                    .filter(r -> roleName.equalsIgnoreCase((String) r.get("name")))
                    .findFirst()
                    .orElse(null);
            if (match == null) return Mono.error(new IllegalStateException("Role not found: " + roleName));
            String id = (String) match.get("id");
            roleIdCache.put(roleName, id);
            return Mono.just(id);
        });
    }

    public Mono<Void> assignRoleToUser(String userId, String roleName) {
        return Mono.zip(managementToken(), getRoleIdByName(roleName))
                .flatMap(tuple -> {
                    String token = tuple.getT1();
                    String roleId = tuple.getT2();
                    return webClient.post()
                            .uri("https://" + domain + "/api/v2/roles/{roleId}/users", roleId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("users", List.of(userId)))
                            .retrieve()
                            .toBodilessEntity()
                            .then();
                });
    }
}
