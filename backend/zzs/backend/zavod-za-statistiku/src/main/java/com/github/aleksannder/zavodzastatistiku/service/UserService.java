package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.client.MupClient;
import com.github.aleksannder.zavodzastatistiku.dto.UserInfoDto;
import com.github.aleksannder.zavodzastatistiku.dto.auth.UserRegisterRequestDto;
import com.github.aleksannder.zavodzastatistiku.dto.auth.UserSyncRequest;
import com.github.aleksannder.zavodzastatistiku.model.User;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import com.github.aleksannder.zavodzastatistiku.repository.UserRepository;
import com.github.aleksannder.zavodzastatistiku.service.sso.Auth0ManagementClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final Auth0ManagementClient auth0ManagementClient;
    private final UserRepository userRepository;
    private final MupClient mupClient;

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
                .city(registerRequest.city())
                .address(registerRequest.address())
                .dateOfBirth(registerRequest.dateOfBirth())
                .gender(registerRequest.gender())
                .auth0UserId(auth0UserId)
                .role(Role.valueOf(roleCitizen))
                .region(registerRequest.region())
                .build();

        u.setJmbg(generateJmbg(u.getDateOfBirth()));

        userRepository.save(u);

        auth0ManagementClient.assignRoleToUser(auth0UserId, roleCitizen).block();

        auth0ManagementClient.updateUserMetadata(auth0UserId, Map.of(
                "service", "zzs",
                "profileId", u.getId().toString()
        )).block();

        sendUserDataToMupService(u);
        return auth0UserId;
    }

    public void updateDbWithUserFromMupService(UserSyncRequest r) {
        User u = User.builder()
                .email(r.email())
                .firstName(r.firstName())
                .lastName(r.lastName())
                .dateOfBirth(r.dateOfBirth())
                .city(r.city())
                .address(r.address())
                .region(r.region())
                .jmbg(r.jmbg())
                .gender(r.gender())
                .createdAt(r.createdAt())
                .role(r.role())
                .auth0UserId(r.auth0UserId())
                .build();

        if (u == null || u.getEmail() == null) {
            log.debug("user email is null");
            return;
        }

        boolean exists = userRepository.existsByEmail(u.getEmail().toLowerCase());
        if (exists) {
            log.error("user email already exists");
            return;
        }

        userRepository.save(u);
    }

    private void sendUserDataToMupService(User u) {
        UserSyncRequest request = new UserSyncRequest(
                u.getEmail(), u.getFirstName(), u.getLastName(),
                u.getDateOfBirth(),u.getCity(),u.getAddress(),
                u.getRegion(),u.getJmbg(),u.getGender(),
                u.getCreatedAt(),u.getRole(),u.getAuth0UserId()
        );

        mupClient.sendUserDataToMupService(request);
    }

    private String generateJmbg(LocalDate dateOfBirth) {
        String day = String.format("%02d", dateOfBirth.getDayOfMonth());
        String month = String.format("%02d", dateOfBirth.getMonthValue());

        String year = String.format("%03d", dateOfBirth.getYear());

        int randNumber = (int) (Math.random() * 900) + 100;
        String rrr = String.valueOf(randNumber);

        int controlNumber = (int) (Math.random() * 9000) + 1000;
        String controlStr =  String.valueOf(controlNumber);

        return day + month + year + rrr + controlStr;
    }

    public UserInfoDto getUserInfo(String userEmail) {
        User u = userRepository.findByEmail(userEmail).orElseThrow();

        return new UserInfoDto(
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getCity(),
                u.getAddress(),
                u.getRegion(),
                u.getGender(),
                u.getJmbg()
        );
    }

    public UserInfoDto updateUser(UserInfoDto dto, String userEmail) {
        User u =  userRepository.findByEmail(userEmail).orElseThrow();

        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setRegion(dto.region());
        u.setCity(dto.city());
        u.setAddress(dto.address());
        u.setGender(dto.gender());

        userRepository.save(u);

        return dto;
    }
}
