package be.kdg.piggybankmodulith.grandparents.adapter.out;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SavingsJpaRepository extends JpaRepository<SavingsPromiseJpaEntity, UUID> {
    Optional<SavingsPromiseJpaEntity> findByGrandchild(UUID owner);
}