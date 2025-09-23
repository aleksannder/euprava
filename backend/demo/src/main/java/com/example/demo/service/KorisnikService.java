package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.RegisterResponse;
import com.example.demo.model.Korisnik;
import com.example.demo.model.Role;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class KorisnikService {

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private JwtService jwtService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$");

    public RegisterResponse registracija(RegisterRequest request) {
        if (korisnikRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email je već registrovan");
        }

        if (!PASSWORD_PATTERN.matcher(request.getLozinka()).matches()) {
            throw new IllegalArgumentException(
                    "Lozinka mora imati najmanje 8 karaktera, uključujući veliko i malo slovo i specijalni karakter"
            );
        }

        if (request.getDatumRodjenja() == null || !request.getDatumRodjenja().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Datum rođenja mora biti u prošlosti");
        }

        Korisnik korisnik = new Korisnik();
        korisnik.setIme(request.getIme());
        korisnik.setPrezime(request.getPrezime());
        korisnik.setEmail(request.getEmail());
        korisnik.setLozinka(passwordEncoder.encode(request.getLozinka()));
        korisnik.setRola(Role.CITIZEN);
        korisnik.setDatumRodjenja(request.getDatumRodjenja());
        korisnik.setJmbg(generisiJmbg(request.getDatumRodjenja()));
        korisnik.setGrad(request.getGrad());
        korisnik.setAdresa(request.getAdresa());
        korisnik.setPol(request.getPol());

        Korisnik savedUser = korisnikRepository.save(korisnik);
        return new RegisterResponse(
                savedUser.getKorisnikID(),
                savedUser.getIme(),
                savedUser.getPrezime(),
                savedUser.getEmail(),
                savedUser.getGrad(),
                savedUser.getAdresa(),
                savedUser.getJmbg(),
                savedUser.getPol()
        );

    }

    public LoginResponse prijava(LoginRequest request) {
        Korisnik korisnik = korisnikRepository.findByEmail(request.getEmail())
                .filter(k -> passwordEncoder.matches(request.getLozinka(), k.getLozinka()))
                .orElseThrow(() -> new IllegalArgumentException("Neispravan email ili lozinka"));

        String token = jwtService.generateToken(korisnik.getEmail(), korisnik.getRola());

        return new LoginResponse(token);
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

}
