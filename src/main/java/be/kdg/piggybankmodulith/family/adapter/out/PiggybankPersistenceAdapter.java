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
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(1)
class PiggybankPersistenceAdapter implements PiggybankLoadPort, PiggybankUpdatePort {

    private final PiggybankEventJpaRepository repository;

    PiggybankPersistenceAdapter(PiggybankEventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Piggybank> loadByOwnerId(GrandchildId ownerId, EntryWindow window) {
        if (window.isAll()) {
            List<PiggybankEventJpaEntity> history = repository.findByOwnerOrderByVersionAsc(ownerId.uuid());
            return history.isEmpty() ? Optional.empty() : Optional.of(replay(history));
        }

        // only read the events in the window; the created event tells whether the piggybank exists at all
        return repository.findByOwnerAndVersion(ownerId.uuid(), 1)
                .map(created -> Piggybank.replay(
                        PiggybankId.of(created.getPiggybankId()),
                        ownerId,
                        repository.findInWindow(ownerId.uuid(), window.start(), window.end()).stream()
                                .map(this::toDomainEvent)
                                .toList(),
                        window));
    }

    @Override
    public List<Piggybank> loadAll() {
        Map<UUID, List<PiggybankEventJpaEntity>> historyPerPiggybank = new LinkedHashMap<>();
        repository.findAllByOrderByPiggybankIdAscVersionAsc().forEach(entity ->
                historyPerPiggybank.computeIfAbsent(entity.getPiggybankId(), id -> new ArrayList<>()).add(entity));

        return historyPerPiggybank.values().stream()
                .map(this::replay)
                .toList();
    }

    @Override
    public void update(Piggybank piggybank) {
        int version = piggybank.getEventStore().size();
        List<PiggybankEventJpaEntity> newEvents = new ArrayList<>();
        for (DomainEvent event : piggybank.getDomainEvents()) {
            newEvents.add(toJpaEntity(piggybank, ++version, event));
        }
        repository.saveAll(newEvents);
    }

    private Piggybank replay(List<PiggybankEventJpaEntity> history) {
        PiggybankEventJpaEntity first = history.getFirst();
        return Piggybank.replay(
                PiggybankId.of(first.getPiggybankId()),
                GrandchildId.of(first.getOwner()),
                history.stream().map(this::toDomainEvent).toList());
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

    private PiggybankEventJpaEntity toJpaEntity(Piggybank piggybank, int version, DomainEvent event) {
        UUID piggybankId = piggybank.getPiggybankId().uuid();
        UUID owner = piggybank.getOwner().uuid();
        return switch (event) {
            case PiggybankCreatedEvent created -> new PiggybankEventJpaEntity(
                    created.eventUUID(), piggybankId, owner, version,
                    PiggybankEventType.CREATED, null, created.eventPit());
            case MoneyDepositedEvent deposited -> new PiggybankEventJpaEntity(
                    deposited.eventUUID(), piggybankId, owner, version,
                    PiggybankEventType.DEPOSITED, deposited.amount().value(), deposited.eventPit());
            case MoneyWithdrawalEvent withdrawn -> new PiggybankEventJpaEntity(
                    withdrawn.eventUUID(), piggybankId, owner, version,
                    PiggybankEventType.WITHDRAWN, withdrawn.amount().value(), withdrawn.eventPit());
            default -> throw new IllegalArgumentException("Unknown piggybank event: " + event);
        };
    }
}
