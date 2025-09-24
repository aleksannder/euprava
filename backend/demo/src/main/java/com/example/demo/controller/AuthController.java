package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Korisnik;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private KorisnikService korisnikService;
    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String response = korisnikService.registerAndLinkToAuth0(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "auth0UserId", response,
                    "success", true
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        try {
//            LoginResponse response = korisnikService.prijava(request);
//            return ResponseEntity.ok(response);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška prilikom prijave");
//        }
//    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token nedostaje");
            }

            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);
            Korisnik korisnik = korisnikRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Korisnik ne postoji"));

            return ResponseEntity.ok(new UserProfileResponse(
                    korisnik.getKorisnikID(),
                    korisnik.getIme(),
                    korisnik.getPrezime(),
                    korisnik.getEmail(),
                    korisnik.getRola()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
