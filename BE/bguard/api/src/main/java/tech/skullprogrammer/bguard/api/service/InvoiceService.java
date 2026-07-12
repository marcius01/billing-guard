package tech.skullprogrammer.bguard.api.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.mapper.InvoiceMapper;
import tech.skullprogrammer.bguard.api.operator.FilterSpecificationFactory;
import tech.skullprogrammer.bguard.api.operator.InvoiceChecker;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Customer;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;
import tech.skullprogrammer.bguard.domain.repository.InvoiceRepository;

import java.util.Map;

//@AllArgsConstructor
@Service
@Validated
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

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public Page<Invoice> getAllInvoices(FilterForRequest filters, Pageable pagination) {
        Specification<Invoice> filtersSpec = Specification
                .where(FilterSpecificationFactory.hasCustomerId(filters.getCustomerId()))
                .and(FilterSpecificationFactory.hasSupplyPointId(filters.getSupplyPointId()))
                .and(FilterSpecificationFactory.hasStatus(filters.getStatus()))
                .and(FilterSpecificationFactory.hasIssueDateFrom(filters.getIssueDateFrom()))
                .and(FilterSpecificationFactory.hasIssueDateTo(filters.getIssueDateTo()));
        return invoiceRepository.findAll(filtersSpec, pagination);
    }

    @Transactional
    public Invoice saveInvoice(@Valid InvoiceDTO invoiceDTO) {
        if ((invoiceDTO.getCustomerId() == null || invoiceDTO.getSupplyPointId() == null)
                && (invoiceDTO.getCustomerCode() == null || invoiceDTO.getSupplyPointCode() == null)) {
            throw new SkullException(SkullException.ErrorType.INVALID_DATA);
        }
        Customer customer = invoiceDTO.getCustomerId() != null ? customerService.getCustomerById(invoiceDTO.getCustomerId()) : customerService.getCustomerByCode(invoiceDTO.getCustomerCode());
        if (customer == null) throw new SkullException(SkullException.ErrorType.CUSTOMER_NOT_FOUND);
        SupplyPoint supplyPoint = invoiceDTO.getSupplyPointId() != null ? supplyPointService.getSupplyPointByIdAndCustomerId(invoiceDTO.getSupplyPointId(), invoiceDTO.getCustomerId()) : supplyPointService.getSupplyPointByCodeAndCustomerCode(invoiceDTO.getSupplyPointCode(), invoiceDTO.getCustomerCode());
        if (supplyPoint == null) throw new SkullException(SkullException.ErrorType.SUPPLY_POINT_NOT_FOUND);
        Map<String, String> errors = InvoiceChecker.isInvoiceConsistent(invoiceDTO);
        if (!errors.isEmpty()) throw new SkullException(SkullException.ErrorType.INVALID_DATA, errors);
        if (invoiceRepository.existsByInvoiceNumberAndSupplyPointId(invoiceDTO.getInvoiceNumber(), invoiceDTO.getSupplyPointId())) throw new SkullException(SkullException.ErrorType.INVOICE_ALREADY_EXISTS, errors);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        return invoiceRepository.save(invoice);
    }
}
