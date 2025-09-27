package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.domain.DomainResponse;
import com.github.aleksannder.zavodzastatistiku.model.Domain;
import com.github.aleksannder.zavodzastatistiku.service.DomainService;
import com.github.aleksannder.zavodzastatistiku.util.DomainConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domain")
@RequiredArgsConstructor
public class DomainController {

    private final DomainService domainService;
    private final DomainConverter domainConverter;

    @GetMapping("/{domainId}")
    public ResponseEntity<DomainResponse> getDomainById(@PathVariable Long domainId) {
        Domain domain = domainService.getById(domainId);
        if (domain == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(domainConverter.toDomainResponse(domain));
    }

    @GetMapping()
    public ResponseEntity<List<DomainResponse>> getAllDomains() {
        List<Domain> domains = domainService.getAll();
        return ResponseEntity.ok(domainConverter.toDomainResponse(domains));
    }

    @DeleteMapping("/{domainId}")
    public ResponseEntity<?> deleteDomainById(@PathVariable Long domainId) {
        if (domainService.getById(domainId) == null) return ResponseEntity.notFound().build();
        domainService.deleteById(domainId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{domainId}/subdomains")
    public ResponseEntity<DomainResponse> getDomainWithSubdomains(@PathVariable Long domainId) {
        if (domainId == null) return ResponseEntity.notFound().build();
        Domain domainsWithSubdomains = domainService.getDomainWithSubdomains(domainId);
        return ResponseEntity.ok(domainConverter.toDomainResponse(domainsWithSubdomains));
    }

    @PostMapping()
    public ResponseEntity<DomainResponse> createDomain(@RequestBody Domain domain) {
        Domain saved = domainService.save(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(domainConverter.toDomainResponse(saved));
    }

    @PutMapping("/{domainId}")
    public ResponseEntity<DomainResponse> updateDomain(@PathVariable Long domainId, @RequestBody Domain domain) {
        Domain existing = domainService.getById(domainId);
        if (existing == null) return ResponseEntity.notFound().build();
        Domain updated = domainService.save(domain);
        return ResponseEntity.ok(domainConverter.toDomainResponse(updated));
    }
}
