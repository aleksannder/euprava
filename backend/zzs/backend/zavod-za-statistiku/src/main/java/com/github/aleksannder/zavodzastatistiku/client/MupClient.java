package com.github.aleksannder.zavodzastatistiku.client;

import com.github.aleksannder.zavodzastatistiku.dto.auth.UserSyncRequest;
import com.github.aleksannder.zavodzastatistiku.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mup-service", url = "http://localhost:8081/api")
public interface MupClient {

    @GetMapping("/stats/identification-cards")
    Long countIdentificationCards();

    @GetMapping("/stats/vehicles/licences")
    Long countVehicleLicences();

    @GetMapping("/stats/drivers-licenses")
    Long countDriversLicenses();

    @GetMapping("/stats/guns/permits")
    Long countGunPermits();

    @PostMapping("/auth/update/mup")
    Void sendUserDataToMupService(@RequestBody UserSyncRequest u);
}
