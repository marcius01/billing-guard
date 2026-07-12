package tech.skullprogrammer.bguard.api.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.skullprogrammer.bguard.api.dto.ImportJobDTO;
import tech.skullprogrammer.bguard.api.mapper.ImportJobMapper;
import tech.skullprogrammer.bguard.api.service.JobService;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;

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
}
