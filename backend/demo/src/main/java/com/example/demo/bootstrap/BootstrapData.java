package com.example.demo.bootstrap;

import com.example.demo.model.Korisnik;
import com.example.demo.model.Role;
import com.example.demo.repository.KorisnikRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BootstrapData {

    @Bean
    CommandLineRunner init(KorisnikRepository korisnikRepository) {
        return args -> {

            if (!korisnikRepository.existsByEmail("admin@gmail.com")) {

                Korisnik admin = Korisnik.builder()
                        .ime("Admin")
                        .prezime("User")
                        .email("admin@gmail.com")
                        .rola(Role.EMPLOYER)
                        .lozinka(new BCryptPasswordEncoder().encode("Voki25r2003!"))
                        .build();

                korisnikRepository.save(admin);
            } else {
                System.out.println("Admin korisnik vec postoji.");
            }
        };
    }
}
