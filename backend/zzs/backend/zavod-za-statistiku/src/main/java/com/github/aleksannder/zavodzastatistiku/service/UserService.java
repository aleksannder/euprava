package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.auth.UserRegisterRequestDto;
import com.github.aleksannder.zavodzastatistiku.model.User;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import com.github.aleksannder.zavodzastatistiku.repository.UserRepository;
import com.github.aleksannder.zavodzastatistiku.service.sso.Auth0ManagementClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    private final Auth0ManagementClient auth0ManagementClient;
    private final UserRepository userRepository;

    @Value("${auth0.m2m.db.connection}")
    private String connection;

    @Value("${auth0.m2m.roles.citizen}")
    private String roleCitizen;

    public String registerAndLinkToAuth0(UserRegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email().toLowerCase())) {
            throw new IllegalArgumentException("email already exists");
        }

        Map<String, Object> auth0User = auth0ManagementClient
                .createDbUser(registerRequest.email(), registerRequest.password(), registerRequest.firstName(), registerRequest.lastName(), connection)
                .block();
        String auth0UserId = (String) Objects.requireNonNull(auth0User).get("user_id");

        User u = User.builder()
                .email(registerRequest.email().toLowerCase())
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .auth0UserId(auth0UserId)
                .roles(Set.of(Role.valueOf(roleCitizen)))
                .build();

        userRepository.save(u);

        auth0ManagementClient.assignRoleToUser(auth0UserId, roleCitizen).block();

        auth0ManagementClient.updateUserMetadata(auth0UserId, Map.of(
                "service", "zzs",
                "profileId", u.getId().toString()
        )).block();

        return auth0UserId;
    }
}
