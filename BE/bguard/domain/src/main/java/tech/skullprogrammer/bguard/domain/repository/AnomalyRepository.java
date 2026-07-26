package tech.skullprogrammer.bguard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tech.skullprogrammer.bguard.domain.entity.Anomaly;
import tech.skullprogrammer.bguard.domain.entity.Invoice;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Long>, JpaSpecificationExecutor<Anomaly> {
}
