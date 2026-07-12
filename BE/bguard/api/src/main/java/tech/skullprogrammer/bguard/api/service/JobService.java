package tech.skullprogrammer.bguard.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tech.skullprogrammer.bguard.api.dto.InvoiceDTO;
import tech.skullprogrammer.bguard.api.dto.JobCsv;
import tech.skullprogrammer.bguard.api.dto.JobCsvRow;
import tech.skullprogrammer.bguard.api.dto.JobCsvRowError;
import tech.skullprogrammer.bguard.api.mapper.InvoiceMapper;
import tech.skullprogrammer.bguard.api.operator.CsvReader;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;
import tech.skullprogrammer.bguard.domain.entity.ImportError;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;
import tech.skullprogrammer.bguard.domain.entity.Invoice;
import tech.skullprogrammer.bguard.domain.enumeration.EImportJobStatus;
import tech.skullprogrammer.bguard.domain.repository.ImportErrorRepository;
import tech.skullprogrammer.bguard.domain.repository.ImportJobRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class JobService {

    private final InvoiceService invoiceService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final AnomalyService anomalyService;
    private final InvoiceMapper invoiceMapper;
    private final ImportJobRepository importJobRepository;
    private final ImportErrorRepository importErrorRepository;

    public JobService(InvoiceService invoiceService, AnomalyDetectionService anomalyDetectionService, AnomalyService anomalyService, InvoiceMapper invoiceMapper, ImportJobRepository importJobRepository, ImportErrorRepository importErrorRepository) {
        this.invoiceService = invoiceService;
        this.anomalyDetectionService = anomalyDetectionService;
        this.anomalyService = anomalyService;
        this.invoiceMapper = invoiceMapper;
        this.importJobRepository = importJobRepository;
        this.importErrorRepository = importErrorRepository;
    }

    @Transactional
    public ImportJob uploadJobs(MultipartFile multipartFile) {
        LocalDateTime startedAt = LocalDateTime.now();
        JobCsv jobCsv = readCsv(multipartFile);
        List<Invoice> savedInvoices = saveJobs(jobCsv);
        ImportJob importJob = generateImportJob(jobCsv);
        List<Anomaly> anomalies = generateAnomalies(savedInvoices, importJob, jobCsv);
        LocalDateTime completedAt = LocalDateTime.now();
        importJob.setStartedAt(startedAt);
        importJob.setCompletedAt(completedAt);
        importJob.setAnomalyRows(anomalies.size());
        saveImportJob(importJob);
        return importJob;
    }

    private List<Anomaly> generateAnomalies(List<Invoice> savedInvoices,  ImportJob importJob, JobCsv jobCsv) {
        List<Anomaly> anomalies = anomalyDetectionService.checkForAnomalies(savedInvoices, importJob);
        this.anomalyService.saveAnomalies(anomalies);
        return anomalies;
    }

    private void saveImportJob(ImportJob importJob) {
        this.importJobRepository.save(importJob);
    }

    private JobCsv readCsv(MultipartFile multipartFile) {
        try {
            String filename = multipartFile.getOriginalFilename();
            JobCsv jobCsv = CsvReader.readJobImportCsv(multipartFile.getInputStream());
            jobCsv.setFilename(filename);
            return jobCsv;
        } catch (IOException e) {
            throw new SkullException(SkullException.ErrorType.INVALID_DATA);
        }
    }

    private List<Invoice> saveJobs(JobCsv jobCsv) {
        List<Invoice> invoices = new ArrayList<>();
        Iterator<JobCsvRow> iterator = jobCsv.getRows().iterator();
        while (iterator.hasNext()) {
            JobCsvRow row = iterator.next();
            InvoiceDTO invoiceDTO = invoiceMapper.toDTO(row);
            try {

                Invoice invoice = invoiceService.saveInvoice(invoiceDTO);
                invoices.add(invoice);
            } catch (SkullException e) {
                jobCsv.getErrors().add(generateError(row, e));
                iterator.remove();
            }
        }
        return invoices;
    }

    private JobCsvRowError generateError(JobCsvRow row, SkullException e) {
        if (e.getErrorType() == SkullException.ErrorType.CUSTOMER_NOT_FOUND) {
            return JobCsvRowError.builder().rowNumber(row.getLineNumber()).field("customerCode").message("customer not found").build();
        }
        if (e.getErrorType() == SkullException.ErrorType.SUPPLY_POINT_NOT_FOUND) {
            return JobCsvRowError.builder().rowNumber(row.getLineNumber()).field("supplyPointCode").message("supply point not found").build();
        }
        return JobCsvRowError.builder().rowNumber(row.getLineNumber()).field("N/D").message(e.getMessage()).build();

    }

    private ImportJob generateImportJob(JobCsv jobCsv) {
        ImportJob importJob = new ImportJob();
        importJob.setFilename(jobCsv.getFilename());
        importJob.setTotalRows(jobCsv.getRows().size() + jobCsv.getErrors().size());
        importJob.setProcessedRows(jobCsv.getRows().size());
        importJob.setDiscardedRows(jobCsv.getErrors().size());
        importJob.setStatus(importJob.getProcessedRows() <= 0 ? EImportJobStatus.COMPLETED : EImportJobStatus.COMPLETED_WITH_ERRORS);
        List<ImportError> errors = jobCsv.getErrors().stream().map(error -> generateImportError(error, importJob)).toList();
        importJob.setErrors(errors);
        return importJob;
    }

    private ImportError generateImportError(JobCsvRowError error, ImportJob importJob) {
        ImportError importError = new ImportError();
        importError.setRowNumber(error.getRowNumber());
        importError.setFieldName(error.getField());
        importError.setRawValue(error.getRawValue());
        importError.setMessage(error.getMessage());
        importError.setCreatedAt(LocalDate.now());
        importError.setErrorCode(error.getErrorCode());
        importError.setImportJob(importJob);
        return importError;
    }

}