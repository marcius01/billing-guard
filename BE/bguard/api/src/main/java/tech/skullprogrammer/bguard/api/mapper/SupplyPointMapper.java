package tech.skullprogrammer.bguard.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.dto.SupplyPointRequest;
import tech.skullprogrammer.bguard.api.dto.SupplyPointResponse;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplyPointMapper {

    SupplyPointResponse toResponseDto(SupplyPoint supplyPoint);
    List<SupplyPointResponse> toResponseDto(List<SupplyPoint> supplyPoints);
    SupplyPoint toEntity(SupplyPointRequest supplyPointRequest);
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "numberOfElements", source = "numberOfElements")
    @Mapping(target = "page", source = "number")
    PaginationResponse<SupplyPointResponse> toResponseDto(Page<SupplyPoint> page);
}
