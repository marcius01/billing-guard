package tech.skullprogrammer.bguard.api.operator;

import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceChecker {

    public static Map<String, String> isInvoiceConsistent(InvoiceDTO invoiceDTO) {
        if (invoiceDTO == null) return Map.of();
        Map<String, String> errors = new HashMap<>();
        if (invoiceDTO.getPaidAmount() != null && invoiceDTO.getPaymentDate() == null) {
            errors.put("paidAmount-Date", "paidAmount must be null if paymentDate is null");
        }
        if (List.of(EInvoiceStatus.ISSUED, EInvoiceStatus.UNPAID).contains(invoiceDTO.getStatus())) {
            if(invoiceDTO.getPaidAmount() != null && invoiceDTO.getPaidAmount() !=0){
                errors.put("paidAmount-Status", "paidAmount must be null if status is ISSUED or UNPAID");
            }
        }
        if (EInvoiceStatus.PARTIALLY_PAID.equals(invoiceDTO.getStatus())) {
            if(invoiceDTO.getPaidAmount() == null || invoiceDTO.getPaidAmount() == 0 || invoiceDTO.getPaidAmount() >= invoiceDTO.getAmount()) {
                errors.put("paidAmount-Status", "paidAmount must be greater than 0 and less than amount if status is PARTIALLY_PAID");
            }
        }
        if (EInvoiceStatus.PAID.equals(invoiceDTO.getStatus())) {
            if(invoiceDTO.getPaidAmount() == null || !invoiceDTO.getPaidAmount().equals(invoiceDTO.getAmount())) {
                errors.put("paidAmount-Status", "paidAmount must be equal to amount if status is PAID");
            }
        }
        return errors;
    }
}
