package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PiggybankCreatedEvent;
import be.kdg.piggybankmodulith.family.domain.EntryWindow;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.PiggybankId;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankSnapshotPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Stores a piggybank as its events, and rebuilds it by replaying them: from its last snapshot, when it has one.
 */
@Component
@Order(1)
class PiggybankPersistenceAdapter implements PiggybankLoadPort, PiggybankUpdatePort, PiggybankSnapshotPort {

    private final PiggybankEventJpaRepository repository;
    private final PiggybankSnapshotJpaRepository snapshotRepository;

    PiggybankPersistenceAdapter(PiggybankEventJpaRepository repository,
                                PiggybankSnapshotJpaRepository snapshotRepository) {
        this.repository = repository;
        this.snapshotRepository = snapshotRepository;
    }

    @Override
    public Optional<Piggybank> loadByOwnerId(GrandchildId ownerId, EntryWindow window) {
        Optional<PiggybankEventJpaEntity> created = repository.findByOwnerAndType(ownerId.uuid(), PiggybankEventType.CREATED);
        if (window.isAll()) {
            return created.map(this::replayFromLastSnapshot);
        }

        // only read the events in the window; the created event tells whether the piggybank exists at all
        return created.map(event -> Piggybank.replay(
                PiggybankId.of(event.getPiggybankId()),
                ownerId,
                toDomainEvents(repository.findInWindow(ownerId.uuid(), window.start(), window.end())),
                window));
    }

    @Override
    public List<Piggybank> loadAll() {
        return repository.findByType(PiggybankEventType.CREATED).stream()
                .map(this::replayFromLastSnapshot)
                .toList();
    }

    @Override
    public void update(Piggybank piggybank) {
        repository.saveAll(piggybank.getDomainEvents().stream()
                .map(event -> toJpaEntity(piggybank, event))
                .toList());
    }

    @Override
    public void saveSnapshot(Piggybank piggybank) {
        LocalDateTime date = piggybank.getBaseSnapshotDate()
                .orElseThrow(() -> new IllegalArgumentException("The piggybank has no snapshot to save"));
        snapshotRepository.save(new PiggybankSnapshotJpaEntity(
                UUID.randomUUID(), piggybank.getPiggybankId().uuid(), date, piggybank.getBaseSnapshotBalance().value()));
    }

    private Piggybank replayFromLastSnapshot(PiggybankEventJpaEntity created) {
        PiggybankId piggybankId = PiggybankId.of(created.getPiggybankId());
        GrandchildId owner = GrandchildId.of(created.getOwner());

        return snapshotRepository.findFirstByPiggybankIdOrderBySnapshotDateDesc(piggybankId.uuid())
                .map(snapshot -> Piggybank.replayFromSnapshot(
                        piggybankId, owner,
                        new Money(snapshot.getBalance()), snapshot.getSnapshotDate(),
                        toDomainEvents(repository.findByPiggybankIdAndPointInTimeGreaterThanEqualOrderByPointInTimeAsc(
                                piggybankId.uuid(), snapshot.getSnapshotDate()))))
                .orElseGet(() -> Piggybank.replay(piggybankId, owner,
                        toDomainEvents(repository.findByOwnerOrderByPointInTimeAsc(owner.uuid()))));
    }

    private List<DomainEvent> toDomainEvents(List<PiggybankEventJpaEntity> entities) {
        return entities.stream().map(this::toDomainEvent).toList();
    }

    private DomainEvent toDomainEvent(PiggybankEventJpaEntity entity) {
        return switch (entity.getType()) {
            case CREATED -> new PiggybankCreatedEvent(
                    entity.getPointInTime(), entity.getEventId(), entity.getOwner());
            case DEPOSITED -> new MoneyDepositedEvent(
                    entity.getPointInTime(), entity.getEventId(), entity.getOwner(), new Money(entity.getAmount()));
            case WITHDRAWN -> new MoneyWithdrawalEvent(
                    entity.getPointInTime(), entity.getEventId(), entity.getOwner(), new Money(entity.getAmount()));
        };
    }

    private PiggybankEventJpaEntity toJpaEntity(Piggybank piggybank, DomainEvent event) {
        UUID piggybankId = piggybank.getPiggybankId().uuid();
        UUID owner = piggybank.getOwner().uuid();
        return switch (event) {
            case PiggybankCreatedEvent created -> new PiggybankEventJpaEntity(
                    created.eventUUID(), piggybankId, owner,
                    PiggybankEventType.CREATED, null, created.eventPit());
            case MoneyDepositedEvent deposited -> new PiggybankEventJpaEntity(
                    deposited.eventUUID(), piggybankId, owner,
                    PiggybankEventType.DEPOSITED, deposited.amount().value(), deposited.eventPit());
            case MoneyWithdrawalEvent withdrawn -> new PiggybankEventJpaEntity(
                    withdrawn.eventUUID(), piggybankId, owner,
                    PiggybankEventType.WITHDRAWN, withdrawn.amount().value(), withdrawn.eventPit());
            default -> throw new IllegalArgumentException("Unknown piggybank event: " + event);
        };
    }
}
