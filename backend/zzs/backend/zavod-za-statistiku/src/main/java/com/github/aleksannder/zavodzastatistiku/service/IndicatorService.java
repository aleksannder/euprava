package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.indicator.IndicatorRequest;
import com.github.aleksannder.zavodzastatistiku.model.Indicator;
import com.github.aleksannder.zavodzastatistiku.model.Subdomain;
import com.github.aleksannder.zavodzastatistiku.repository.IndicatorRepository;
import com.github.aleksannder.zavodzastatistiku.repository.SubdomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndicatorService {

    private final IndicatorRepository repository;
    private final SubdomainRepository subdomainRepository;

    public Indicator create(IndicatorRequest r) {
        Subdomain s = subdomainRepository.findById(r.subdomainId())
                .orElseThrow(() -> new IllegalArgumentException("Subdomain not found"));
        Indicator i = Indicator.builder()
                .code(r.code())
                .name(r.name())
                .description(r.description())
                .subdomain(s)
                .build();

        return repository.save(i);
    }

    public List<Indicator> getAll() {
        return repository.findAll();
    }

    public Indicator getById(Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    public Indicator update(Long id, IndicatorRequest r) {
        Indicator exist = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Indicator not found"));
        exist.setCode(r.code());
        exist.setName(r.name());
        exist.setDescription(r.description());
        exist.setSubdomain(subdomainRepository.findById(r.subdomainId()).orElseThrow(() -> new IllegalArgumentException("Subdomain not found")));
        return repository.save(exist);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Indicator> getBySubdomainId(Long subdomainId) {
        return repository.findAllBySubdomainId(subdomainId);
    }
}
