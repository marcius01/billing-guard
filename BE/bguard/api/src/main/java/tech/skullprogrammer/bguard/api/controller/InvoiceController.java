package tech.skullprogrammer.bguard.api.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.dto.PaginationForRequest;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.mapper.InvoiceMapper;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.InvoiceService;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EInvoiceStatus;

import java.net.URI;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;

    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @GetMapping(value = "/{id}")
    public InvoiceDTO getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return invoiceMapper.toDTO(invoice);
    }

    @GetMapping
    public PaginationResponse<InvoiceDTO> getAllInvoices(
            @ModelAttribute FilterForRequest<EInvoiceStatus> filters,
            @Valid @ModelAttribute PaginationForRequest pagination
            ){
        Pageable pageable = PageRequestFactory.create(pagination.getPage(), pagination.getSize(), pagination.getSort());
        Page<Invoice> allInvoices = invoiceService.getAllInvoices(filters, pageable);
        return invoiceMapper.toResponseDto(allInvoices);
    }

    @PostMapping
    public ResponseEntity<Void> saveInvoice(@RequestBody @Valid InvoiceDTO invoiceDTO){
        Invoice invoice = invoiceService.saveInvoice(invoiceDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(invoice.getId()).toUri();
        return ResponseEntity.created(location)
                .build();
    }
}
