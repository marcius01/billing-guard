package tech.skullprogrammer.bguard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;

@Repository
public interface SupplyPointRepository extends JpaRepository<SupplyPoint, Long> {
}
