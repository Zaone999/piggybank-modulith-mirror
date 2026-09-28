package be.kdg.piggybankmodulith.grandparents.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface GrandchildJpaRepository extends JpaRepository<GrandchildJpaEntity, UUID> {
}
