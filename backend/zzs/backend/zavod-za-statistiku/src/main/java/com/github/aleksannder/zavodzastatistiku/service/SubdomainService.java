package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.dto.domain.subdomain.SubdomainRequest;
import com.github.aleksannder.zavodzastatistiku.model.Domain;
import com.github.aleksannder.zavodzastatistiku.model.Subdomain;
import com.github.aleksannder.zavodzastatistiku.repository.DomainRepository;
import com.github.aleksannder.zavodzastatistiku.repository.SubdomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubdomainService {

    private final SubdomainRepository subdomainRepository;
    private final DomainRepository domainRepository;

    public List<Subdomain> getAll() {
        return subdomainRepository.findAll();
    }

    public Subdomain getById(Long id) {
        return subdomainRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        subdomainRepository.deleteById(id);
    }

    public Subdomain create(SubdomainRequest request) {
        Domain domain = domainRepository.findById(request.domainId())
                .orElseThrow(() -> new IllegalArgumentException("Domain not found"));
        Subdomain subdomain = Subdomain.builder()
                .code(request.code())
                .name(request.name())
                .domain(domain)
                .build();
        return subdomainRepository.save(subdomain);
    }


}
