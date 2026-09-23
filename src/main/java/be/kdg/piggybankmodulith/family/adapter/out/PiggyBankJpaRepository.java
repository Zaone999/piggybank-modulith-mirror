package be.kdg.piggybankmodulith.family.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PiggyBankJpaRepository extends JpaRepository<PiggybankJpaEntity, UUID> {
    Optional<PiggybankJpaEntity> findByOwner(UUID owner);
}
