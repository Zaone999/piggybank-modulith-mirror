package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromiseId;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(1)
class SavingsPromisePersistenceAdapter implements SavingsPromiseLoadPort, SavingsPromiseUpdatePort {

    private final SavingsPromiseJpaRepository repository;

    SavingsPromisePersistenceAdapter(SavingsPromiseJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SavingsPromise> loadActivePromiseFor(GrandChildId grandchildId) {
        return repository.findFirstByGrandchildAndTriggeredFalse(grandchildId.uuid())
                .map(this::toDomain);
    }

    @Override
    public void update(SavingsPromise savingsPromise) {
        repository.save(toJpaEntity(savingsPromise));
    }

    private SavingsPromise toDomain(SavingsPromiseJpaEntity entity) {
        return SavingsPromise.restore(
                SavingsPromiseId.of(entity.getUuid()),
                GrandChildId.of(entity.getGrandchild()),
                new Money(entity.getBalance()),
                new Money(entity.getPromiseBoundary()),
                entity.isTriggered());
    }

    private SavingsPromiseJpaEntity toJpaEntity(SavingsPromise savingsPromise) {
        return new SavingsPromiseJpaEntity(
                savingsPromise.getSavingsPromiseId().uuid(),
                savingsPromise.getGrandchild().uuid(),
                savingsPromise.getBalance().value(),
                savingsPromise.getPromiseBoundary().value(),
                savingsPromise.isTriggered());
    }
}
