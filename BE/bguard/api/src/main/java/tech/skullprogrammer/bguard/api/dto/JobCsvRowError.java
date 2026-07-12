package tech.skullprogrammer.bguard.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobCsvRowError {
    private long rowNumber;
    private String field;
    private String rawValue;
    private String message;
    private String errorCode;
}
