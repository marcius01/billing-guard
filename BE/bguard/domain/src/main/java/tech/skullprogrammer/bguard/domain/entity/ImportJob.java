package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tech.skullprogrammer.bguard.domain.enumeration.EImportJobStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class ImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
    @OneToMany(mappedBy = "importJob")
    private List<ImportError> errors;
}
