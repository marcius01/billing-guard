package tech.skullprogrammer.bguard.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import tech.skullprogrammer.bguard.api.dto.AnomalyResponse;
import tech.skullprogrammer.bguard.api.dto.CustomerRequest;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnomalyMapper {

    AnomalyResponse toDTO(Anomaly anomaly);
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "numberOfElements", source = "numberOfElements")
    @Mapping(target = "page", source = "number")
    PaginationResponse<AnomalyResponse> toDTO(Page<Anomaly> page);
}
