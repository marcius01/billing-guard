package tech.skullprogrammer.bguard.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointStatus;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyPointRequest {

    @NotBlank
    private String code;
    @NotNull
    private ESupplyPointType type;
    @NotBlank
    private String region;
    @NotBlank
    private String city;
    private ESupplyPointStatus status;
    private LocalDate createdAt;
    @NotNull
    private Long customerId;
}
