package tech.skullprogrammer.bguard.api.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointType;

import java.time.LocalDate;

@Data
public class JobCsvRow {

    private Long lineNumber;
    @CsvBindByName(required = true)
    private String customerCode;
    @CsvBindByName
    private String customerName;
    @CsvBindByName(required = true)
    private String supplyPointCode;
    @CsvBindByName
    private ESupplyPointType supplyPointType;
    @CsvBindByName(required = true)
    private String invoiceNumber;
    @CsvBindByName(required = true)
    @CsvDate
    private LocalDate issueDate;
    @CsvBindByName(required = true)
    @CsvDate
    private LocalDate dueDate;
    @CsvBindByName
    @CsvDate
    private LocalDate paymentDate;
    @CsvBindByName(required = true)
    @CsvDate
    private LocalDate periodStart;
    @CsvBindByName(required = true)
    @CsvDate
    private LocalDate periodEnd;
    @CsvBindByName(required = true)
    private Double amount;
    @CsvBindByName
    private Double paidAmount;

}
