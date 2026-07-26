package tech.skullprogrammer.bguard.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tech.skullprogrammer.bguard.api.dto.AnomalyResponse;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.mapper.AnomalyMapper;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.AnomalyService;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyStatus;

@RestController
public class AnomalyController {

    private final AnomalyService anomalyService;
    private final AnomalyMapper mapper;

    public AnomalyController(AnomalyMapper mapper, AnomalyService anomalyService) {
        this.mapper = mapper;
        this.anomalyService = anomalyService;
    }

    @GetMapping(value = "/anomalies")
    PaginationResponse<AnomalyResponse> getAnomalies (
            @ModelAttribute FilterForRequest<EAnomalyStatus> filterForRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
            ) {
        Pageable pageable = PageRequestFactory.create(page, size, sort);
        return mapper.toDTO(anomalyService.getAnomalies(filterForRequest, pageable));
    }

    @GetMapping(value = "/anomalies/{id}")
    AnomalyResponse getAnomalyById(@PathVariable Long id) {
        return mapper.toDTO(anomalyService.getAnomalyById(id));
    }

    @PostMapping(value = "/anomalies/{id}/resolve")
    AnomalyResponse resolveAnomaly(@PathVariable Long id) {
        return mapper.toDTO(anomalyService.resolveAnomaly(id));
    }

    @PostMapping(value = "/anomalies/{id}/ignore")
    AnomalyResponse ignoreAnomaly(@PathVariable Long id) {
        return mapper.toDTO(anomalyService.ignoreAnomaly(id));
    }
}
