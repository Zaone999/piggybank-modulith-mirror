package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.family.domain.OwnerId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.PiggybankId;
import be.kdg.piggybankmodulith.family.port.out.LoadPiggybankPort;
import be.kdg.piggybankmodulith.family.port.out.UpdatePiggybankPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PiggybankJpaAdapter implements UpdatePiggybankPort, LoadPiggybankPort {

    private static final Logger log = LoggerFactory.getLogger(PiggybankJpaAdapter.class);

    private final PiggyBankJpaRepository piggybanks;

    public PiggybankJpaAdapter(PiggyBankJpaRepository piggybanks) {
        this.piggybanks = piggybanks;
    }

    @Override
    public Optional<Piggybank> loadBy(OwnerId owner) {
        return this.piggybanks.findByOwner(owner.uuid())
                .map(entity -> new Piggybank(PiggybankId.of(entity.getUuid()), OwnerId.of(entity.getOwner()), entity.getBalance()));
    }

    @Override
    public List<Piggybank> loadAll() {
        List<PiggybankJpaEntity> piggybankJpaEntities = this.piggybanks.findAll();
        return piggybankJpaEntities.stream()
                .map(entity -> new Piggybank(PiggybankId.of(entity.getUuid()), OwnerId.of(entity.getOwner()), entity.getBalance()))
                .toList();
    }

    @Override
    public Piggybank update(Piggybank piggybank) {
        PiggybankJpaEntity entity = new PiggybankJpaEntity(piggybank.getPiggybankId().uuid(), piggybank.getOwner().uuid(), piggybank.getBalance());
        PiggybankJpaEntity saved = this.piggybanks.save(entity);
        return new Piggybank(PiggybankId.of(saved.getUuid()), OwnerId.of(saved.getOwner()), saved.getBalance());
    }
}
