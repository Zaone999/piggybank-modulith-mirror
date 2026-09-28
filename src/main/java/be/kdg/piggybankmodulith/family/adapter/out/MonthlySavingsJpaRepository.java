package be.kdg.piggybankmodulith.family.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

interface MonthlySavingsJpaRepository extends JpaRepository<MonthlySavingsJpaEntity, UUID> {

    Optional<MonthlySavingsJpaEntity> findByOwnerAndMonth(UUID owner, LocalDate month);

    // the question the read model is shaped for: one row, no replay
    Optional<MonthlySavingsJpaEntity> findFirstByOwnerOrderByBalanceDescMonthAsc(UUID owner);
}
