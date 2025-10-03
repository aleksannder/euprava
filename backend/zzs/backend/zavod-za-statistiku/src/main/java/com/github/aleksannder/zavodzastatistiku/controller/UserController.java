package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.UserInfoDto;
import com.github.aleksannder.zavodzastatistiku.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('CITIZEN', 'ANALYST')")
    @GetMapping("/{userEmail}")
    public UserInfoDto getUserInfo(@PathVariable String userEmail) {
        return userService.getUserInfo(userEmail);
    }

    @PreAuthorize("hasRole('CITIZEN')")
    @PostMapping("/{userEmail}/update")
    public UserInfoDto updateUser(@PathVariable String userEmail, @RequestBody UserInfoDto userInfo) {
        return userService.updateUser(userInfo, userEmail);
    }
}
