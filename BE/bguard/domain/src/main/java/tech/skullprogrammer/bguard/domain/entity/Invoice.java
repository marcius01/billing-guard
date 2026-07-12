package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String invoiceNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Double amount;
    private Double paidAmount;
    @Enumerated(EnumType.STRING)
    private EInvoiceStatus status;
    @ManyToOne
    private SupplyPoint supplyPoint;
    @ManyToOne
    private Customer customer;
}
