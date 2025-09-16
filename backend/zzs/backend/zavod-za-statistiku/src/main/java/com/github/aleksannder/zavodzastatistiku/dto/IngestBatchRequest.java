package com.github.aleksannder.zavodzastatistiku.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record IngestBatchRequest(
        @NotBlank String batchId,
        @NotNull Instant generatedAt,
        @NotEmpty List<RecordItem> records) {
    public record RecordItem(
            @NotNull String municipalityCode,
            @NotNull Integer age,
            @NotBlank String gender,
            @NotBlank String docType
    ) {}
}
