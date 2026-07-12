package tech.skullprogrammer.bguard.api.dto;

import tech.skullprogrammer.bguard.domain.entity.ImportError;
import tech.skullprogrammer.bguard.domain.enumeration.EImportJobStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ImportJobDTO {

    private String filename;
    private EImportJobStatus status;
    private int totalRows;
    private int processedRows;
    private int discardedRows;
    private int anomalyRows;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorMessage;
    private List<ImportErrorDTO> errors;

}
