package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.grandparents.domain.GrandchildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.port.out.LoadSavingsPromisePort;
import be.kdg.piggybankmodulith.grandparents.port.out.UpdateSavingsPromisePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SavingsPromiseJpaAdapter implements UpdateSavingsPromisePort, LoadSavingsPromisePort {

    private final SavingsJpaRepository savingsRepository;

    public SavingsPromiseJpaAdapter(SavingsJpaRepository savingsRepository) {
        this.savingsRepository = savingsRepository;
    }

    @Override
    public Optional<SavingsPromise> loadBy(GrandchildId grandchildId) {
        return this.savingsRepository.findByGrandchild(grandchildId.uuid())
                .map(entity -> new SavingsPromise(GrandchildId.of(entity.getGrandchild()), Money.valueOf(entity.getBalance()), Money.valueOf(entity.getPromise())));
    }

    @Override
    public List<SavingsPromise> loadAll() {
        List<SavingsPromiseJpaEntity> savingsJpaEntities = this.savingsRepository.findAll();
        return savingsJpaEntities.stream()
                .map(entity -> new SavingsPromise(GrandchildId.of(entity.getGrandchild()),  Money.valueOf(entity.getBalance()),Money.valueOf(entity.getPromise())))
                .toList();
    }

    @Override
    public SavingsPromise update(SavingsPromise savingsPromise) {
        SavingsPromiseJpaEntity entity = new SavingsPromiseJpaEntity(savingsPromise.getGrandchild().uuid(), savingsPromise.getBalance().getValue(), savingsPromise.getPromiseBoundary().getValue() );
        SavingsPromiseJpaEntity saved = this.savingsRepository.save(entity);
        return new SavingsPromise(GrandchildId.of(saved.getGrandchild()),  Money.valueOf(saved.getBalance()),  Money.valueOf(saved.getPromise()));
    }
}