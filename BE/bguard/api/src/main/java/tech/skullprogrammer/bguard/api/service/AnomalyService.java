package tech.skullprogrammer.bguard.api.service;

import org.springframework.stereotype.Service;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;
import tech.skullprogrammer.bguard.domain.repository.AnomalyRepository;

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

}
