package tech.skullprogrammer.bguard.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import tech.skullprogrammer.bguard.api.dto.SupplyPointResponse;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplyPointMapper {

    SupplyPointResponse toResponseDto(SupplyPoint supplyPoint);
}
