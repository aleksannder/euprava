package com.github.aleksannder.zavodzastatistiku.service;

import com.github.aleksannder.zavodzastatistiku.model.Domain;
import com.github.aleksannder.zavodzastatistiku.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainService {

    private final DomainRepository domainRepository;

    public Domain getById(Long id) {
        return domainRepository.getDomainById(id).orElse(null);
    }

    public List<Domain> getAll() {
        return domainRepository.findAllWithSubdomains();
    }

    public void deleteById(Long id) {
        domainRepository.deleteById(id);
    }

    public Domain getDomainWithSubdomains(Long domainId) {
        return domainRepository.getDomainByIdWithSubdomains(domainId);
    }

    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }
}
