package tech.skullprogrammer.bguard.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import tech.skullprogrammer.bguard.api.dto.CustomerRequest;
import tech.skullprogrammer.bguard.api.dto.CustomerResponse;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.domain.entity.Customer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    Customer toEntity(CustomerRequest customerRequest);
    CustomerRequest toRequestDto(Customer customer);
    CustomerResponse toResponseDto(Customer customer);
    @Mapping(target = "totalElements", source = "numberOfElements")
    @Mapping(target = "page", source = "number")
    PaginationResponse<CustomerResponse> toRequestDto(Page<Customer> page);
}
