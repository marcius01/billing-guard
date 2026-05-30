package tech.skullprogrammer.bguard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.skullprogrammer.bguard.domain.entity.SupplyPoint;
import tech.skullprogrammer.bguard.domain.enumeration.ESupplyPointType;

@Repository
public interface SupplyPointRepository extends JpaRepository<SupplyPoint, Long> {

    boolean existsByTypeAndCode(ESupplyPointType type, String code);
}
