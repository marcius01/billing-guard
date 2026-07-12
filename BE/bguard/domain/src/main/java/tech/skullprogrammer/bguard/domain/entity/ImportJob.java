package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import tech.skullprogrammer.bguard.domain.enumeration.EImportJobStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String errorMessage;
    @OneToMany(mappedBy = "importJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImportError> errors;
}
