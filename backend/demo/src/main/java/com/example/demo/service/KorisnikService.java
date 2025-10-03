package com.example.demo.service;

import com.example.demo.client.ZzsClient;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserSyncRequest;
import com.example.demo.model.Korisnik;
import com.example.demo.model.Role;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.service.auth.Auth0ManagementClient;
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
public class KorisnikService {

    private final Auth0ManagementClient client;

    private final KorisnikRepository korisnikRepository;

    private final ZzsClient zzsClient;

    @Value("${auth0.m2m.db.connection}")
    private String connection;

    @Value("${auth0.m2m.roles.citizen}")
    private String roleCitizen;

    public String registerAndLinkToAuth0(RegisterRequest request) {
        if (korisnikRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("email exists");
        }

        Map<String, Object> auth0User = client
                .createDbUser(request.getEmail(), request.getLozinka(), request.getIme(), request.getPrezime(), connection)
                .block();
        String auth0UserId = (String) Objects.requireNonNull(auth0User.get("user_id"));

        Korisnik k = Korisnik.builder()
                .email(request.getEmail())
                .firstName(request.getIme())
                .lastName(request.getPrezime())
                .gender(request.getPol())
                .dateOfBirth(request.getDatumRodjenja())
                .role(Role.CITIZEN)
                .city(request.getGrad())
                .address(request.getAdresa())
                .jmbg(generisiJmbg(request.getDatumRodjenja()))
                .region(request.getRegion())
                .auth0UserId(auth0UserId)
                .build();

        korisnikRepository.save(k);

        client.assignRoleToUser(auth0UserId, roleCitizen).block();

        client.updateUserMetadata(auth0UserId, Map.of(
                "service", "mup",
                "profileId", k.getId().toString()
        )).block();

        sendUserDataToStatisticsService(k);
        return auth0UserId;
    }

    private String generisiJmbg(LocalDate datumRodjenja) {
        String dan = String.format("%02d", datumRodjenja.getDayOfMonth());
        String mesec = String.format("%02d", datumRodjenja.getMonthValue());

        String godina = String.format("%03d", datumRodjenja.getYear() % 1000);

        int randomBroj = (int) (Math.random() * 900) + 100;
        String rrr = String.valueOf(randomBroj);

        int dodatak = (int) (Math.random() * 9000) + 1000;
        String ccck = String.valueOf(dodatak);

        return dan + mesec + godina + rrr + ccck;
    }

    private void sendUserDataToStatisticsService(Korisnik korisnik) {
        UserSyncRequest request = new UserSyncRequest(
                korisnik.getEmail(), korisnik.getFirstName(), korisnik.getLastName(),
                korisnik.getDateOfBirth(),korisnik.getCity(),korisnik.getAddress(),
                korisnik.getRegion(),korisnik.getJmbg(),korisnik.getGender(),
                korisnik.getCreatedAt(),korisnik.getRole(),korisnik.getAuth0UserId()
        );
        zzsClient.sendUserDataToStatisticsService(request);
    }

    public void updateDbWithUserFromStatisticsService(UserSyncRequest r) {
        Korisnik u = Korisnik.builder()
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
            log.debug("[MupService] updateDbWithUSerFromStatisticsService user is null");
            return;
        }

        boolean exists = korisnikRepository.existsByEmail(u.getEmail());

        if (exists) {
            log.error("[MupService] updateDbWithUSerFromStatisticsService user already exists");
            return;
        }

        korisnikRepository.save(u);
    }
}
