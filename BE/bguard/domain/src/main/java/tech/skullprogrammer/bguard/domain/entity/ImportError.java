package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ImportError {
    //id, importJobId, rowNumber, fieldName, rawValue, errorCode, message, createdAt
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int rowNumber;
    private String fieldName;
    private String rawValue;
    private String errorCode;
    private String message;
    private LocalDate createdAt;
    @ManyToOne
    private ImportJob importJob;
}
