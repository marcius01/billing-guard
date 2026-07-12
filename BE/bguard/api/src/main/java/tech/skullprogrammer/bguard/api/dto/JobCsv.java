package tech.skullprogrammer.bguard.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobCsv {
    String filename;
    private List<JobCsvRow> rows;
    private List<JobCsvRowError> errors;
}
