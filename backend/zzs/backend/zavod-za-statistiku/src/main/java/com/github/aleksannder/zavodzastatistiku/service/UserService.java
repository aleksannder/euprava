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
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User registerUser(UserRegisterRequestDto userRegisterRequestDto) throws ValidationException {
        if (userRepository.existsByEmail(userRegisterRequestDto.email().toLowerCase())) {
            throw new ValidationException("Email already exists");
        }

        User u = User.builder()
                        .firstName(userRegisterRequestDto.firstName())
                                .lastName(userRegisterRequestDto.lastName())
                                        .email(userRegisterRequestDto.email())
                                                .password(passwordEncoder.encode(userRegisterRequestDto.password()))
                                                        .createdAt(Instant.now()).build();


        switch (userRegisterRequestDto.role()) {
            case ADMIN:
                throw new ValidationException("Admin can't register!");
            case ANALYST:
                throw new ValidationException("Analyst can't register!");
            case CITIZEN:
                u.setRoles(Set.of(Role.ANALYST));
                break;
            default:
                throw new ValidationException("Invalid role");
        }

        return userRepository.save(u);

    }
}
