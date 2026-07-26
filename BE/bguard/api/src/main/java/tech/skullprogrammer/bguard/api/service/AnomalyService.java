package tech.skullprogrammer.bguard.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.skullprogrammer.bguard.api.dto.ExplanationResponse;
import tech.skullprogrammer.bguard.api.dto.FilterForRequest;
import tech.skullprogrammer.bguard.domain.SkullException;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;
import tech.skullprogrammer.bguard.domain.enumeration.EAnomalyStatus;
import tech.skullprogrammer.bguard.domain.repository.AnomalyRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnomalyService {

    private AnomalyRepository anomalyRepository;

    public AnomalyService(AnomalyRepository anomalyRepository) {
        this.anomalyRepository = anomalyRepository;
    }

    public Anomaly saveAnomaly(Anomaly anomaly) {
        return anomalyRepository.save(anomaly);
    }

    public List<Anomaly> saveAnomalies(List<Anomaly> anomalies) {
        return anomalyRepository.saveAll(anomalies);
    }

    public Page<Anomaly> getAnomalies(FilterForRequest<EAnomalyStatus> filterForRequest, Pageable pagination) {
        return anomalyRepository.findAll(filterForRequest.toSpecification(), pagination);
    }

    public Anomaly getAnomalyById(Long id) {
        return anomalyRepository.findById(id).orElse(null);
    }

    @Transactional
    public Anomaly resolveAnomaly(Long id) {
        return changeStatus(id, EAnomalyStatus.RESOLVED);
    }

    @Transactional
    public Anomaly ignoreAnomaly(Long id) {
        return changeStatus(id, EAnomalyStatus.IGNORED);
    }

    private Anomaly changeStatus(Long id, EAnomalyStatus status) {
        Anomaly anomaly = anomalyRepository.findById(id).orElse(null);
        if (anomaly == null) throw new SkullException(SkullException.ErrorType.ENTITY_NOT_FOUND);
        if (anomaly.getStatus().equals(status)) throw new SkullException(SkullException.ErrorType.UNCHANGED_DATA);
        anomaly.setStatus(status);
        anomaly.setResolvedAt(LocalDateTime.now());
        anomaly.setResolvedBy("N/D");
        return anomalyRepository.save(anomaly);
    }

    public ExplanationResponse getExplanation(Long id) {
        Anomaly anomaly = anomalyRepository.findById(id).orElse(null);
        if (anomaly == null) throw new SkullException(SkullException.ErrorType.ENTITY_NOT_FOUND);
        String explanationString = "Anomaly " +
        anomaly.getType().name() + " (severity " + anomaly.getSeverity().name() + "): " + anomaly.getDescription();
        return new ExplanationResponse(explanationString);
    }
}
