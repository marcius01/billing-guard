package tech.skullprogrammer.bguard.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.dto.SupplyPointRequest;
import tech.skullprogrammer.bguard.api.dto.SupplyPointResponse;
import tech.skullprogrammer.bguard.api.mapper.SupplyPointMapper;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.SupplyPointService;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;

import java.net.URI;
import java.util.List;

@RestController
public class SupplyPointController {

    private SupplyPointService supplyPointService;
    private SupplyPointMapper supplyPointMapper;

    public SupplyPointController(SupplyPointService supplyPointService, SupplyPointMapper supplyPointMapper) {
        this.supplyPointService = supplyPointService;
        this.supplyPointMapper = supplyPointMapper;
    }

    @GetMapping(value = "/supply-points")
    public PaginationResponse<SupplyPointResponse> getAllSupplyPoints(
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero() int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(value = "sort", defaultValue = "name,asc") String sort
    ) {
        Pageable pageable = PageRequestFactory.create(page, size, sort);
        return supplyPointMapper.toResponseDto(supplyPointService.getAllSupplyPoint(pageable));
    }

    @GetMapping(value = "/supply-points/{id}")
    public SupplyPointResponse getSupplyPointById(@PathVariable("id") Long id) {
        return supplyPointMapper.toResponseDto(supplyPointService.getSupplyPointById(id));
    }

    @PostMapping(value = "/supply-points")
    public ResponseEntity<Void> saveSupplyPoint(@RequestBody @Valid SupplyPointRequest supplyPointRequest) {
        SupplyPoint supplyPoint = supplyPointService.saveSupplyPoint(supplyPointRequest);
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(supplyPoint.getId()).toUri();
        return ResponseEntity.created(locationUri)
                .build();
    }

}
