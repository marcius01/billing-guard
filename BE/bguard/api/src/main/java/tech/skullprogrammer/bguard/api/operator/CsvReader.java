package tech.skullprogrammer.bguard.api.operator;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import tech.skullprogrammer.bguard.api.dto.JobCsv;
import tech.skullprogrammer.bguard.api.dto.JobCsvRow;
import tech.skullprogrammer.bguard.api.dto.JobCsvRowError;
import tech.skullprogrammer.bguard.domain.SkullException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class CsvReader {

    public static JobCsv readJobImportCsv(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        HeaderColumnNameMappingStrategy<JobCsvRow> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(JobCsvRow.class);

        List<JobCsvRow> validRows = new ArrayList<>();
        List<JobCsvRowError> errors = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(
                        new CSVParserBuilder()
                                .withSeparator(';')
                                .withIgnoreLeadingWhiteSpace(true)
                                .build()
                )
                .build()) {
            try {
                strategy.captureHeader(csvReader);
            } catch (Throwable e) {
                throw new SkullException(SkullException.ErrorType.CSV_ERROR, Map.of("message", e.getMessage()));
            }
            String[] row;
            long rowCounter = 1;
            InfoRow infoRow;
            while ((infoRow = readNext(csvReader)).isProcessable()) {
                rowCounter++;
                try {
                    row = infoRow.getRow();
                    JobCsvRow dto = strategy.populateNewBean(row);
                    dto.setLineNumber(rowCounter);
                    validRows.add(dto);
                } catch (Throwable e) {
//                    log.error("Error reading row {}", rowCounter, e);
                    errors.add(buildRowError(rowCounter, e));
                }
            }
        }
        return JobCsv.builder().rows(validRows).errors(errors).build();
    }

    private static InfoRow readNext(CSVReader csvReader) {
        try {
            return new InfoRow(null, csvReader.readNext());
        } catch (Throwable e) {
            return new InfoRow(e, null);
        }
    }

    private static JobCsvRowError buildRowError(long rowCounter, Throwable cause) {
        if (cause instanceof CsvRequiredFieldEmptyException ex) {
            String field = ex.getDestinationField().getName();
            return JobCsvRowError.builder().rowNumber(rowCounter).field(field).message("required").build();
        } else if (cause instanceof CsvDataTypeMismatchException ex) {
            String value = String.valueOf(ex.getSourceObject());
            return JobCsvRowError.builder().rowNumber(rowCounter).field("N/D").rawValue(value).message("wrong data format for value " + value).build();
        } else {
            return JobCsvRowError.builder().rowNumber(rowCounter).field("N/D").message(cause.getMessage()).build();
        }
    }

    private record InfoRow(Throwable cause, String[] row) {
        boolean isProcessable() {
            return cause != null || row != null;
        }
        String[] getRow() throws Throwable{
            if (cause != null) throw cause;
            return row;
        }
    }
}
