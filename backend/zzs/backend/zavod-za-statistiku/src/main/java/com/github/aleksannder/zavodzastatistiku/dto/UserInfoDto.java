package com.github.aleksannder.zavodzastatistiku.dto;

import com.github.aleksannder.zavodzastatistiku.model.enums.Region;

public record UserInfoDto(
        String firstName, String lastName,
        String email, String city,
        String address, Region region,
        String gender, String jmbg
) {
}
