package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.PiggybankId;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Order(1)
class PiggybankPersistenceAdapter implements PiggybankLoadPort, PiggybankUpdatePort {

    private final PiggybankJpaRepository repository;

    PiggybankPersistenceAdapter(PiggybankJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Piggybank> loadByOwnerId(GrandchildId ownerId) {
        return repository.findByOwner(ownerId.uuid())
                .map(this::toDomain);
    }

    @Override
    public List<Piggybank> loadAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void update(Piggybank piggybank) {
        repository.save(toJpaEntity(piggybank));
    }

    private Piggybank toDomain(PiggybankJpaEntity entity) {
        return Piggybank.restore(
                PiggybankId.of(entity.getUuid()),
                GrandchildId.of(entity.getOwner()),
                new Money(entity.getBalance()));
    }

    private PiggybankJpaEntity toJpaEntity(Piggybank piggybank) {
        return new PiggybankJpaEntity(
                piggybank.getPiggybankId().uuid(),
                piggybank.getOwner().uuid(),
                piggybank.getBalance().value());
    }
}
