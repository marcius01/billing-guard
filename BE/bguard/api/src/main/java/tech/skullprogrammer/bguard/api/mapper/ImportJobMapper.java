package tech.skullprogrammer.bguard.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import tech.skullprogrammer.bguard.api.dto.ImportJobDTO;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportJobMapper {

    ImportJobDTO toDTO (ImportJob importJob);
    List<ImportJobDTO> toDTO (List<ImportJob> importJobs);
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "numberOfElements", source = "numberOfElements")
    @Mapping(target = "page", source = "number")
    PaginationResponse<ImportJobDTO> toResponseDto(Page<ImportJob> page);
}
