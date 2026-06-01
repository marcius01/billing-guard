package tech.skullprogrammer.bguard.api.operator;

import org.springframework.data.jpa.domain.Specification;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.time.LocalDate;

public class FilterSpecificationFactory {

    public static Specification<Invoice> hasCustomerId(Long customerId) {
        return (root, query, cb) ->
                customerId == null ? null : cb.equal(root.get("customer").get("id"), customerId);
    }
    public static Specification<Invoice> hasSupplyPointId(Long supplyPointId) {
        return (root, query, cb) ->
                supplyPointId == null ? null : cb.equal(root.get("supplyPoint").get("id"), supplyPointId);
    }
    public static Specification<Invoice> hasStatus(EInvoiceStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }
    public static Specification<Invoice> hasIssueDateFrom(LocalDate issueDateFrom) {
        return (root, query, cb) ->
                issueDateFrom == null ? null : cb.greaterThanOrEqualTo(root.get("issueDate"), issueDateFrom);
    }
    public static Specification<Invoice> hasIssueDateTo(LocalDate issueDateTo) {
        return (root, query, cb) ->
                issueDateTo == null ? null : cb.lessThanOrEqualTo(root.get("issueDate"), issueDateTo);
    }
}
