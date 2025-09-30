package com.github.aleksannder.zavodzastatistiku.controller;

import com.github.aleksannder.zavodzastatistiku.dto.domain.subdomain.SubdomainRequest;
import com.github.aleksannder.zavodzastatistiku.dto.domain.subdomain.SubdomainResponse;
import com.github.aleksannder.zavodzastatistiku.model.Subdomain;
import com.github.aleksannder.zavodzastatistiku.service.SubdomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subdomains")
@RequiredArgsConstructor
public class SubdomainController {

    private final SubdomainService subdomainService;

    @PostMapping
    public ResponseEntity<SubdomainResponse> create(@RequestBody SubdomainRequest subdomainRequest) {
        Subdomain saved = subdomainService.create(subdomainRequest);
        return ResponseEntity.ok(new SubdomainResponse(
                saved.getId(), saved.getCode(), saved.getName(), saved.getDomain().getId()
        ));
    }

    @GetMapping
    public ResponseEntity<List<SubdomainResponse>> getAll() {
        List<SubdomainResponse> result = subdomainService.getAll().stream()
                .map(s -> new SubdomainResponse(s.getId(), s.getCode(), s.getName(), s.getDomain().getId()))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{subdomainId}")
    public ResponseEntity<SubdomainResponse> get(@PathVariable Long subdomainId) {
        Subdomain s = subdomainService.getById(subdomainId);
        if (s == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new SubdomainResponse(s.getId(), s.getCode(), s.getName(), s.getDomain().getId()));
    }

    @DeleteMapping("/{subdomainId}")
    public ResponseEntity<?> delete(@PathVariable Long subdomainId) {
        subdomainService.delete(subdomainId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<SubdomainResponse> getSubdomainByCode(@PathVariable String code) {
        Subdomain s = subdomainService.getByCode(code);
        if (s == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new SubdomainResponse(s.getId(), s.getCode(), s.getName(), s.getDomain().getId()));
    }

}
