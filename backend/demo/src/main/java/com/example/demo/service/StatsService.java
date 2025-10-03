package com.example.demo.service;

import com.example.demo.model.StatusZahteva;
import com.example.demo.repository.LicnaKartaRepository;
import com.example.demo.repository.OruzjeRepository;
import com.example.demo.repository.SaobracajnaDozvolaRepository;
import com.example.demo.repository.VozackaDozvolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final LicnaKartaRepository licnaKartaRepository;
    private final VozackaDozvolaRepository vozackaDozvolaRepository;
    private final SaobracajnaDozvolaRepository saobracajnaDozvolaRepository;
    private final OruzjeRepository oruzjeRepository;

    private static final StatusZahteva STATUS = StatusZahteva.DOZVOLJEN;

    public Long countIds() {
        return licnaKartaRepository.countIdentityCardByStatusEquals(STATUS);
    }

    public Long countVehicleLicences() {
        return saobracajnaDozvolaRepository.countVehicleLicencesByStatusEquals(STATUS);
    }

    public Long countDriversLicenses() {
        return vozackaDozvolaRepository.countDrivingLicensesByStatusEquals(STATUS);
    }

    public Long countGunPermits() {
        return oruzjeRepository.countOruzjeByStatusEquals(STATUS);
    }
}
