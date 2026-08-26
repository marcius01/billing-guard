package tech.skullprogrammer.bguard.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ImportProcessingService {

    private final InvoiceService invoiceService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final AnomalyService anomalyService;
    private final InvoiceMapper invoiceMapper;
    private final ImportErrorRepository importErrorRepository;
    private final ImportJobRepository importJobRepository;

    public ImportProcessingService(InvoiceService invoiceService, AnomalyDetectionService anomalyDetectionService,
                                   AnomalyService anomalyService, InvoiceMapper invoiceMapper, ImportErrorRepository importErrorRepository, ImportJobRepository importJobRepository) {
        this.invoiceService = invoiceService;
        this.anomalyDetectionService = anomalyDetectionService;
        this.anomalyService = anomalyService;
        this.invoiceMapper = invoiceMapper;
        this.importErrorRepository = importErrorRepository;
        this.importJobRepository = importJobRepository;
    }

    @Transactional
    public ImportJob importJob(Long importJobId) {
        ImportJob importJob = importJobRepository.findById(importJobId).orElseThrow(() -> new SkullException("import id " + importJobId + " not found", SkullException.ErrorType.ENTITY_NOT_FOUND));
        LocalDateTime startedAt = LocalDateTime.now();
        if (!EImportJobStatus.CREATED.equals(importJob.getStatus())) throw new SkullException("import status " + importJob.getStatus(), SkullException.ErrorType.IMPORT_JOB_ALREADY_PROCESSED);
        importJob.setStatus(EImportJobStatus.PROCESSING);
        JobCsv jobCsv = readCsv(importJob.getFilename());
        List<Invoice> savedInvoices = saveJobs(jobCsv);
        initImportJob(jobCsv, importJob);
        List<Anomaly> anomalies = generateAnomalies(savedInvoices, importJob, jobCsv);
        LocalDateTime completedAt = LocalDateTime.now();
        importJob.setStartedAt(startedAt);
        importJob.setCompletedAt(completedAt);
        importJob.setAnomalyRows(anomalies.size());
        return importJob;
    }

    private List<Anomaly> generateAnomalies(List<Invoice> savedInvoices, ImportJob importJob, JobCsv jobCsv) {
        List<Anomaly> anomalies = anomalyDetectionService.checkForAnomalies(savedInvoices, importJob);
        this.anomalyService.saveAnomalies(anomalies);
        return anomalies;
    }


    private JobCsv readCsv(String fileName) {
        try (InputStream inputStream = Files.newInputStream(Path.of(fileName))){
            JobCsv jobCsv = CsvReader.readJobImportCsv(inputStream);
            jobCsv.setFilename(fileName);
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

    private ImportJob initImportJob(JobCsv jobCsv, ImportJob importJob) {
//        ImportJob importJob = new ImportJob();
//        importJob.setFilename(jobCsv.getFilename());
        importJob.setTotalRows(jobCsv.getRows().size() + jobCsv.getErrors().size());
        importJob.setProcessedRows(jobCsv.getRows().size());
        importJob.setDiscardedRows(jobCsv.getErrors().size());
        importJob.setStatus(importJob.getProcessedRows() <= 0 ? EImportJobStatus.COMPLETED : EImportJobStatus.COMPLETED_WITH_ERRORS);
        List<ImportError> errors = jobCsv.getErrors().stream().map(error -> generateImportError(error, importJob)).toList();
        importJob.getErrors().clear();
        importJob.getErrors().addAll(errors);
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
