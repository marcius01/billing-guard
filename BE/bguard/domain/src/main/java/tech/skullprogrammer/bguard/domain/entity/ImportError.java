package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ImportError {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long rowNumber;
    private String fieldName;
    private String rawValue;
    private String errorCode;
    private String message;
    private LocalDate createdAt;
    @ManyToOne
    private ImportJob importJob;
}
