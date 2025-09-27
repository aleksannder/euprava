package com.github.aleksannder.zavodzastatistiku.dto.domain;


import com.github.aleksannder.zavodzastatistiku.model.Subdomain;

import java.util.List;

public record DomainResponse(Long id, String code, String name, List<Subdomain> subdomains) {}
