package tech.skullprogrammer.bguard.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.time.LocalDate;

@Builder
@Data
public class InvoiceDTO {
    @NotBlank
    private String invoiceNumber;
    @NotNull
    @PastOrPresent
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    @NotNull
    @PastOrPresent
    private LocalDate periodStart;
    @NotNull
    @PastOrPresent
    private LocalDate periodEnd;
    @NotNull
    //@PositiveOrZero
    private Double amount;
    private Double paidAmount;
    private EInvoiceStatus status;
//    @NotNull
    private Long supplyPointId;
//    @NotNull
    private Long customerId;
    @JsonIgnore
    private String customerCode;
    @JsonIgnore
    private String supplyPointCode;
}
