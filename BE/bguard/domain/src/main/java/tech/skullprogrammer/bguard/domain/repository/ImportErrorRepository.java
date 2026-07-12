package tech.skullprogrammer.bguard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.skullprogrammer.bguard.domain.entity.ImportError;

public interface ImportErrorRepository extends JpaRepository<ImportError, Long> {
}
