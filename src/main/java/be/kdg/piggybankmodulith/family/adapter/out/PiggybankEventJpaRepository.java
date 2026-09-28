package be.kdg.piggybankmodulith.family.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface PiggybankEventJpaRepository extends JpaRepository<PiggybankEventJpaEntity, UUID> {

    List<PiggybankEventJpaEntity> findByOwnerOrderByPointInTimeAsc(UUID owner);

    List<PiggybankEventJpaEntity> findAllByOrderByPointInTimeAsc();

    Optional<PiggybankEventJpaEntity> findByOwnerAndType(UUID owner, PiggybankEventType type);

    List<PiggybankEventJpaEntity> findByType(PiggybankEventType type);

    @Query("""
            select event from PiggybankEventJpaEntity event
            where event.owner = :owner and event.pointInTime >= :start and event.pointInTime < :end
            order by event.pointInTime""")
    List<PiggybankEventJpaEntity> findInWindow(UUID owner, LocalDateTime start, LocalDateTime end);

    List<PiggybankEventJpaEntity> findByPiggybankIdAndPointInTimeGreaterThanEqualOrderByPointInTimeAsc(
            UUID piggybankId, LocalDateTime snapshotDate);
}
