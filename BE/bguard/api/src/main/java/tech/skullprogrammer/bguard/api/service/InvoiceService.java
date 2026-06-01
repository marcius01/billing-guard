package tech.skullprogrammer.bguard.api.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.dto.PaginationForRequest;
import tech.skullprogrammer.bguard.api.mapper.InvoiceMapper;
import tech.skullprogrammer.bguard.api.operator.FilterSpecificationFactory;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.repository.InvoiceRepository;

import java.util.List;

//@AllArgsConstructor
@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;
    private CustomerService customerService;
    private SupplyPointService supplyPointService;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, CustomerService customerService, SupplyPointService supplyPointService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.customerService = customerService;
        this.supplyPointService = supplyPointService;
    }

    public Invoice getInvoiceById(Long id){
        return invoiceRepository.findById(id).orElse(null);
    }

    public Page<Invoice> getAllInvoices(FilterForRequest filters, Pageable pagination){
        Specification<Invoice> filtersSpec = Specification
                .where(FilterSpecificationFactory.hasCustomerId(filters.getCustomerId()))
                .and(FilterSpecificationFactory.hasSupplyPointId(filters.getSupplyPointId()))
                .and(FilterSpecificationFactory.hasStatus(filters.getStatus()))
                .and(FilterSpecificationFactory.hasIssueDateFrom(filters.getIssueDateFrom()))
                .and(FilterSpecificationFactory.hasIssueDateTo(filters.getIssueDateTo()));
        return invoiceRepository.findAll(filtersSpec, pagination);
    }
}
