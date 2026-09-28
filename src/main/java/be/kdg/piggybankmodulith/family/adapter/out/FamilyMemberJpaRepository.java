package be.kdg.piggybankmodulith.family.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface FamilyMemberJpaRepository extends JpaRepository<FamilyMemberJpaEntity, UUID> {
}
