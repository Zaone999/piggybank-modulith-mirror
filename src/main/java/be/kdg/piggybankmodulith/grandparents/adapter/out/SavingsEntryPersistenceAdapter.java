package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsEntry;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsEntryLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsEntryProjectionPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class SavingsEntryPersistenceAdapter implements SavingsEntryLoadPort, SavingsEntryProjectionPort {

    private final SavingsEntryJpaRepository repository;

    SavingsEntryPersistenceAdapter(SavingsEntryJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByEventId(UUID eventId) {
        return repository.existsById(eventId);
    }

    @Override
    public void save(SavingsEntry savingsEntry) {
        repository.save(new SavingsEntryJpaEntity(
                savingsEntry.eventId(),
                savingsEntry.grandchildId().uuid(),
                savingsEntry.type(),
                savingsEntry.amount().value(),
                savingsEntry.pointInTime()));
    }
}
