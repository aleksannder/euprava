package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.auth.UserRegisterRequestDto;
import com.github.aleksannder.zavodzastatistiku.model.User;
import com.github.aleksannder.zavodzastatistiku.model.enums.Role;
import com.github.aleksannder.zavodzastatistiku.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public User registerUser(UserRegisterRequestDto userRegisterRequestDto) throws ValidationException {
        if (userRepository.existsByEmail(userRegisterRequestDto.getEmail())) {
            throw new ValidationException("Email already exists");
        }

        boolean enabled;
        Set<Role> roles;

        switch (userRegisterRequestDto.getRole()) {
            case ANALYST -> {
                enabled = false;
                roles = Set.of(Role.ANALYST);
            }
            case CITIZEN -> {
                enabled = true;
                roles = Set.of(Role.CITIZEN);
            }
            case ADMIN -> {
                throw new ValidationException("Admin role cannot be registered!");
            }
            default -> throw new ValidationException("Invalid role");
        }

        User u = User.builder()
                .email(userRegisterRequestDto.getEmail())
                .firstName(userRegisterRequestDto.getFirstName())
                .lastName(userRegisterRequestDto.getLastName())
                .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                .enabled(enabled)
                .roles(roles)
                .build();

        return userRepository.save(u);
    }

    public User findByEmail(String email) {
        Optional<User> u = userRepository.findByEmail(email);
        return u.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepository.findByEmail(username.toLowerCase()).orElseThrow(() -> new UsernameNotFoundException(username));

        var authorities = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList();

        if (!u.getEnabled()) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(authorities)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(!u.getEnabled())
                .build();
    }
}
