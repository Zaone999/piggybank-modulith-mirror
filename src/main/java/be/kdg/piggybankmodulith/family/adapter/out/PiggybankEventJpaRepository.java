package be.kdg.piggybankmodulith.family.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface PiggybankEventJpaRepository extends JpaRepository<PiggybankEventJpaEntity, UUID> {

    List<PiggybankEventJpaEntity> findByOwnerOrderByVersionAsc(UUID owner);

    List<PiggybankEventJpaEntity> findAllByOrderByPiggybankIdAscVersionAsc();

    // the first event of a piggybank: the created event, whatever window is asked for
    Optional<PiggybankEventJpaEntity> findByOwnerAndVersion(UUID owner, int version);

    @Query("""
            select event from PiggybankEventJpaEntity event
            where event.owner = :owner and event.pointInTime >= :start and event.pointInTime < :end
            order by event.version""")
    List<PiggybankEventJpaEntity> findInWindow(UUID owner, LocalDateTime start, LocalDateTime end);
}
