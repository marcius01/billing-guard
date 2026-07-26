package tech.skullprogrammer.bguard.api.operator;

import org.springframework.data.jpa.domain.Specification;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.time.LocalDate;

public class FilterSpecificationFactory {

    public static <T> Specification<T> hasCustomerId(Long customerId) {
        return (root, query, cb) ->
                customerId == null ? null : cb.equal(root.get("customer").get("id"), customerId);
    }
    public static <T> Specification<T> hasSupplyPointId(Long supplyPointId) {
        return (root, query, cb) ->
                supplyPointId == null ? null : cb.equal(root.get("supplyPoint").get("id"), supplyPointId);
    }
    public static <E extends Enum<E>, T> Specification<T> hasStatus(E status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }
    public static <T> Specification<T> hasIssueDateFrom(LocalDate issueDateFrom) {
        return (root, query, cb) ->
                issueDateFrom == null ? null : cb.greaterThanOrEqualTo(root.get("issueDate"), issueDateFrom);
    }
    public static <T> Specification<T> hasIssueDateTo(LocalDate issueDateTo) {
        return (root, query, cb) ->
                issueDateTo == null ? null : cb.lessThanOrEqualTo(root.get("issueDate"), issueDateTo);
    }
}
