package com.github.aleksannder.zavodzastatistiku.util;

import com.github.aleksannder.zavodzastatistiku.dto.domain.DomainResponse;
import com.github.aleksannder.zavodzastatistiku.model.Domain;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DomainConverter {

    public DomainResponse toDomainResponse(Domain domain) {
        return new DomainResponse(domain.getId(), domain.getCode(), domain.getName(), domain.getSubdomains());
    }

    public List<DomainResponse> toDomainResponse(List<Domain> domains) {
        List<DomainResponse> responses = new ArrayList<>();
        domains.forEach(domain -> responses.add(toDomainResponse(domain)));
        return responses;
    }
}
