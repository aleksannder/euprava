package com.github.aleksannder.zavodzastatistiku.service.csv;

public class InvalidCsvHeaderException extends RuntimeException {
    public InvalidCsvHeaderException(String message) {
        super("[ImportExportCsvService] Invalid CSV Header for domain: " + message);
    }
}
