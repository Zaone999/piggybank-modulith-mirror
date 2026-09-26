package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsEntry;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsEntryLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsEntryProjectionPort;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
class InMemorySavingsEntryAdapter implements SavingsEntryLoadPort, SavingsEntryProjectionPort {

    private final Map<UUID, SavingsEntry> savingsEntries = new LinkedHashMap<>();

    @Override
    public boolean existsByEventId(UUID eventId) {
        return savingsEntries.containsKey(eventId);
    }

    @Override
    public void save(SavingsEntry savingsEntry) {
        savingsEntries.putIfAbsent(savingsEntry.eventId(), savingsEntry);
    }
}
