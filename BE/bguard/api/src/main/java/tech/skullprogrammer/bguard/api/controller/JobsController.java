package tech.skullprogrammer.bguard.api.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.skullprogrammer.bguard.api.dto.ImportJobDTO;
import tech.skullprogrammer.bguard.api.dto.PaginationForRequest;
import tech.skullprogrammer.bguard.api.dto.PaginationResponse;
import tech.skullprogrammer.bguard.api.mapper.ImportJobMapper;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.JobService;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;

import java.util.List;

@RestController
public class JobsController {

    private JobService jobService;
    private ImportJobMapper importJobMapper;

    public JobsController(JobService jobService, ImportJobMapper importJobMapper) {
        this.jobService = jobService;
        this.importJobMapper = importJobMapper;
    }

    @PostMapping(value = "/import-jobs/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImportJobDTO uploadJobs(@RequestPart("file") MultipartFile file) {
        ImportJob importJob = this.jobService.uploadJobs(file);
        return importJobMapper.toDTO(importJob);
    }

    @GetMapping(value = "/import-jobs")
    public PaginationResponse<ImportJobDTO> getAllImportJobs(@Valid @ModelAttribute PaginationForRequest pagination) {
        Pageable pageable = PageRequestFactory.create(pagination.getPage(), pagination.getSize(), pagination.getSort());
        Page<ImportJob> result = this.jobService.getJobs(pageable);
        return importJobMapper.toResponseDto(result);
    }

    @GetMapping(value = "/import-jobs/{id}")
    public ImportJobDTO getImportJobById(@PathVariable Long id) {
        ImportJob job = this.jobService.getJobById(id);
        return importJobMapper.toDTO(job);
    }
}
