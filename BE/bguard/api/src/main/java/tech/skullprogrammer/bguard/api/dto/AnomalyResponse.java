package tech.skullprogrammer.bguard.api.dto;

import lombok.*;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalySeverity;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyStatus;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyResponse {

    private EAnomalyType type;
    private EAnomalySeverity severity;
    private EAnomalyStatus status;
    private String description;
    private String technicalDetails;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private LocalDateTime createdAt;
    private Long invoiceId;
    private Long supplyPointId;
    private Long importJobId;

}
