package tech.skullprogrammer.bguard.api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import tech.skullprogrammer.bguard.api.operator.FilterSpecificationFactory;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.time.LocalDate;

@Data
@Builder
public class FilterForRequest <E extends Enum<E>> {
    private Long customerId;
    private Long supplyPointId;
    private E status;
    private LocalDate issueDateFrom;
    private LocalDate issueDateTo;

    public <T> Specification<T> toSpecification() {
        return Specification
                .where(FilterSpecificationFactory.<T>hasCustomerId(customerId))
                .and(FilterSpecificationFactory.hasSupplyPointId(supplyPointId))
                .and(FilterSpecificationFactory.hasStatus(status))
                .and(FilterSpecificationFactory.hasIssueDateFrom(issueDateFrom))
                .and(FilterSpecificationFactory.hasIssueDateTo(issueDateTo));
    }
}
