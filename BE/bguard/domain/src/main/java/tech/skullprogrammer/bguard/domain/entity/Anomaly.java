package tech.skullprogrammer.bguard.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalySeverity;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyStatus;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Anomaly {
    //id, invoiceId, supplyPointId, importJobId, type, severity, status, description, technicalDetails, createdAt, resolvedAt, resolvedBy
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private EAnomalyType type;
    @Enumerated(EnumType.STRING)
    private EAnomalySeverity severity;
    @Enumerated(EnumType.STRING)
    private EAnomalyStatus status;
    private String description;
    private String technicalDetails;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    @ManyToOne
    private Invoice invoice;
    @ManyToOne
    private SupplyPoint supplyPoint;
    @ManyToOne
    private ImportJob importJob;

}
