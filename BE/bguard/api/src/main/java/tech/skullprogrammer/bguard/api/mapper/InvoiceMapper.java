package tech.skullprogrammer.bguard.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.dto.SupplyPointResponse;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {

    InvoiceDTO toDTO (Invoice invoice);
    List<InvoiceDTO> toDTO (List<Invoice> invoices);
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "numberOfElements", source = "numberOfElements")
    @Mapping(target = "page", source = "number")
    PaginationResponse<InvoiceDTO> toResponseDto(Page<Invoice> page);
    Invoice toEntity(InvoiceDTO invoiceDTO);

}
