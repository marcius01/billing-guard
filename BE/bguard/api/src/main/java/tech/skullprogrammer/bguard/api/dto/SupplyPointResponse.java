package tech.skullprogrammer.bguard.api.dto;

import lombok.Data;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointStatus;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointType;

import java.time.LocalDate;

@Data
public class SupplyPointResponse {

    private String code;
    private ESupplyPointType type;
    private String region;
    private String city;
    private ESupplyPointStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long customerId;

}
