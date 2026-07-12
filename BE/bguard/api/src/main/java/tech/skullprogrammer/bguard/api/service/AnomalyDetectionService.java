package tech.skullprogrammer.bguard.api.service;

import org.springframework.stereotype.Service;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalySeverity;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyStatus;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyType;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnomalyDetectionService {

    public List<Anomaly> checkForAnomalies (List<Invoice> invoices, ImportJob importJob) {
        List<Anomaly> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            List<Anomaly> anomalies = checkAnomaliesForInvoice(invoice, importJob);
            result.addAll(anomalies);
        }
        return result;
    }

    private List<Anomaly> checkAnomaliesForInvoice(Invoice invoice, ImportJob importJob) {
        List<Anomaly> anomalies = new ArrayList();
        if (invoice.getPeriodEnd().isBefore(invoice.getPeriodStart())) {
            anomalies.add(createAnomaly("End date before start date", EAnomalyType.INVALID_DATE_RANGE, EAnomalySeverity.HIGH, invoice, importJob));
        }
        if (invoice.getPaymentDate() != null && invoice.getPaymentDate().isBefore(invoice.getIssueDate())) {
            anomalies.add(createAnomaly("Payment date before issue date", EAnomalyType.PAYMENT_BEFORE_ISSUE_DATE, EAnomalySeverity.MEDIUM, invoice, importJob));
        }
        if (EInvoiceStatus.UNPAID.equals(invoice.getStatus()) && invoice.getDueDate().isBefore(LocalDate.now())) {
            anomalies.add(createAnomaly("Unpaid after due date", EAnomalyType.UNPAID_OVER_THRESHOLD, EAnomalySeverity.MEDIUM, invoice, importJob));
        }
        return anomalies;
    }

    private Anomaly createAnomaly(String description, EAnomalyType type, EAnomalySeverity severity, Invoice invoice, ImportJob importJob) {
        return Anomaly.builder()
                .description(description)
                .createdAt(LocalDateTime.now())
                .type(type)
                .severity(severity)
                .status(EAnomalyStatus.OPEN)
                .invoice(invoice)
                .supplyPoint(invoice.getSupplyPoint())
                .importJob(importJob)
                .build();
    }
}
