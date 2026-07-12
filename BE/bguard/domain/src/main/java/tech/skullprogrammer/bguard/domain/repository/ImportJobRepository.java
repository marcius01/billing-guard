package tech.skullprogrammer.bguard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.skullprogrammer.bguard.domain.entity.ImportJob;

public interface ImportJobRepository extends JpaRepository<ImportJob, Long> {
}
