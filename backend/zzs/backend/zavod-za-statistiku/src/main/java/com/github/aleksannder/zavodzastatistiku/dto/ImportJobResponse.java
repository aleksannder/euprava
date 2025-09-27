package com.github.aleksannder.zavodzastatistiku.dto;

import com.github.aleksannder.zavodzastatistiku.model.ImportJob;

import java.time.Instant;

public record ImportJobResponse(
        Long id,
        String sourceType,
        String status,
        String errorReportPath,
        Instant startedAt,
        Instant finishedAt
) {
    public static ImportJobResponse of(ImportJob job) {
        return new ImportJobResponse(
                job.getId(),
                job.getSourceType(),
                job.getStatus(),
                job.getErrorReportPath(),
                job.getStartedAt(),
                job.getFinishedAt()
        );
    }
}
