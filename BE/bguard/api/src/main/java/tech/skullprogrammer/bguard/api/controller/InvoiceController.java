package tech.skullprogrammer.bguard.api.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.dto.PaginationForRequest;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.mapper.InvoiceMapper;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.InvoiceService;
import tech.skullprogrammer.bguard.domain.entity.Invoice;

import java.net.URI;

@Controller
public class InvoiceController {

    private InvoiceService invoiceService;
    private InvoiceMapper invoiceMapper;

    public InvoiceController(InvoiceService invoiceService, InvoiceMapper invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @GetMapping(value = "/invoice/{id}")
    public InvoiceDTO getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return invoiceMapper.toDTO(invoice);
    }

    @GetMapping(value = "/invoice")
    public PaginationResponse<InvoiceDTO> getAllInvoices(
            @ModelAttribute FilterForRequest filters,
            @Valid @ModelAttribute PaginationForRequest pagination
            ){
        Pageable pageable = PageRequestFactory.create(pagination.getPage(), pagination.getSize(), pagination.getSort());
        Page<Invoice> allInvoices = invoiceService.getAllInvoices(filters, pageable);
        return invoiceMapper.toResponseDto(allInvoices);
    }

    @PostMapping(value = "/invoice")
    public ResponseEntity<Void> saveInvoice(@Valid InvoiceDTO invoiceDTO){
        Invoice invoice = invoiceService.saveInvoice(invoiceDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/invoice/{id}").buildAndExpand(invoice.getId()).toUri();
        return ResponseEntity.created(location)
                .build();
    }
}
