package tech.skullprogrammer.bguard.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.skullprogrammer.bguard.api.dto.SupplyPointResponse;
import tech.skullprogrammer.bguard.api.mapper.SupplyPointMapper;
import tech.skullprogrammer.bguard.api.service.SupplyPointService;

@RestController
public class SupplyPointController {

    private SupplyPointService supplyPointService;
    private SupplyPointMapper supplyPointMapper;

    public SupplyPointController(SupplyPointService supplyPointService, SupplyPointMapper supplyPointMapper) {
        this.supplyPointService = supplyPointService;
        this.supplyPointMapper = supplyPointMapper;
    }

    @GetMapping(value = "/supply-points/{id}")
    public SupplyPointResponse getSupplyPointById(@PathVariable("id") Long id) {
        return supplyPointMapper.toResponseDto(supplyPointService.getSupplyPointById(id));
    }

}
