package tech.skullprogrammer.bguard.test.api.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.operator.PageRequestFactory;
import tech.skullprogrammer.bguard.api.service.JobService;
import tech.skullprogrammer.bguard.domain.entity.ImportError;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;
import tech.skullprogrammer.bguard.test.SpringTestConfiguration;

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest(classes = {SpringTestConfiguration.class})
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/insert-test-invoices.sql"})
@Transactional
public class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Test
    public void testSave() throws IOException {
        MockMultipartFile importJobMultiPart = new MockMultipartFile("file", getClass().getResourceAsStream("/jobs/test-invoices.csv"));
        ImportJob importJob = jobService.uploadJobs(importJobMultiPart);
        Assertions.assertEquals(10, importJob.getTotalRows());
        Assertions.assertEquals(9, importJob.getProcessedRows());
        Assertions.assertEquals(1, importJob.getDiscardedRows());
        Assertions.assertEquals(1, importJob.getAnomalyRows());
    }

    @Test
    @Sql(scripts = {"classpath:db/insert-test-import-jobs.sql"})
    public void testFind() throws IOException {
        Pageable pageable = PageRequestFactory.create(0, 10, "id,desc");
        Page<ImportJob> jobs = jobService.getJobs(pageable);
        Assertions.assertEquals(3, jobs.getTotalElements());
        jobs.stream().filter(job -> job.getId().equals(4002L)).findFirst().ifPresent(
                job -> Assertions.assertEquals(2, job.getErrors().size()));
    }

    @Test
    @Sql(scripts = {"classpath:db/insert-test-import-jobs.sql"})
    public void testFindById() throws IOException {
        ImportJob job = jobService.getJobById(4002L);
        Assertions.assertNotNull(job);
        Assertions.assertEquals(2, job.getErrors().size());
    }
}
