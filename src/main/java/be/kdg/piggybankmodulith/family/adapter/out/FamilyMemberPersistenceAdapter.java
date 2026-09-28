package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.family.domain.FamilyMember;
import be.kdg.piggybankmodulith.family.domain.FamilyMemberId;
import be.kdg.piggybankmodulith.family.port.out.FamilyMemberLoadPort;
import be.kdg.piggybankmodulith.family.port.out.FamilyMemberUpdatePort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(1)
class FamilyMemberPersistenceAdapter implements FamilyMemberLoadPort, FamilyMemberUpdatePort {

    private final FamilyMemberJpaRepository repository;

    FamilyMemberPersistenceAdapter(FamilyMemberJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<FamilyMember> loadById(FamilyMemberId familyMemberId) {
        return repository.findById(familyMemberId.uuid())
                .map(entity -> FamilyMember.restore(
                        FamilyMemberId.of(entity.getUuid()), entity.getFirstName(), entity.getLastName()));
    }

    @Override
    public void update(FamilyMember familyMember) {
        repository.save(new FamilyMemberJpaEntity(
                familyMember.getFamilyMemberId().uuid(),
                familyMember.getFirstName(),
                familyMember.getLastName()));
    }
}
