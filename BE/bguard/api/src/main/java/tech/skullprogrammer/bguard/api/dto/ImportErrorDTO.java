package tech.skullprogrammer.bguard.api.dto;

import jakarta.persistence.*;
import lombok.*;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportErrorDTO {

    private long rowNumber;
    private String fieldName;
    private String rawValue;
    private String errorCode;
    private String message;
    private LocalDate createdAt;

}
