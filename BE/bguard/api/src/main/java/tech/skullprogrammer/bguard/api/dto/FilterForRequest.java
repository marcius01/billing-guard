package tech.skullprogrammer.bguard.api.dto;

import lombok.Builder;
import lombok.Data;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.time.LocalDate;

@Data
@Builder
public class FilterForRequest {
    private Long customerId;
    private Long supplyPointId;
    private EInvoiceStatus status;
    private LocalDate issueDateFrom;
    private LocalDate issueDateTo;
}
