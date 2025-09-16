package com.github.aleksannder.zavodzastatistiku.dto.auth;

import com.github.aleksannder.zavodzastatistiku.model.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserRegisterResponseDto {
    private String email;
    private boolean enabled;
    private Set<?> roles;

    public static UserRegisterResponseDto of(User user) {
        UserRegisterResponseDto userRegisterResponseDto = new UserRegisterResponseDto();
        userRegisterResponseDto.setEmail(user.getEmail());
        userRegisterResponseDto.setEnabled(user.getEnabled());
        userRegisterResponseDto.setRoles(user.getRoles());
        return userRegisterResponseDto;
    }
}
