package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.OwnerId;
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
    public Optional<SavingsPromise> loadBy(OwnerId owner) {
        return this.savingsRepository.findByOwner(owner.uuid())
                .map(entity -> new SavingsPromise(OwnerId.of(entity.getOwner()),  entity.getBalance(), entity.getPromise()));
    }

    @Override
    public List<SavingsPromise> loadAll() {
        List<SavingsPromiseJpaEntity> savingsJpaEntities = this.savingsRepository.findAll();
        return savingsJpaEntities.stream()
                .map(entity -> new SavingsPromise(OwnerId.of(entity.getOwner()),  entity.getBalance(),entity.getPromise()))
                .toList();
    }

    @Override
    public SavingsPromise update(SavingsPromise savingsPromise) {
        SavingsPromiseJpaEntity entity = new SavingsPromiseJpaEntity(savingsPromise.getOwner().uuid(), savingsPromise.getBalance(), savingsPromise.getPromiseBoundary() );
        SavingsPromiseJpaEntity saved = this.savingsRepository.save(entity);
        return new SavingsPromise(OwnerId.of(saved.getOwner()),  saved.getBalance(), saved.getPromise());
    }
}