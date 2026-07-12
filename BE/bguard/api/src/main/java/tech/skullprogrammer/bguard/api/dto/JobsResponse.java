package tech.skullprogrammer.bguard.api.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import tech.skullprogrammer.bguard.domain.enumeration.EImportJobStatus;

import java.time.LocalDate;

@Data
public class JobsResponse {

    private String filename;
    @Enumerated(EnumType.STRING)
    private EImportJobStatus status;
    private int totalRows;
    private int processedRows;
    private int discardedRows;
    private int anomalyRows;
    private LocalDate startedAt;
    private LocalDate completedAt;
    private String errorMessage;
}

