package com.github.aleksannder.zavodzastatistiku.dto;

public record HighlightResponse(
        String label, String value, String icon,
        String domainCode, String subdomainCode, String indicatorCode
) {}
