package be.kdg.piggybankmodulith.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record PiggybankCreatedEvent(LocalDateTime eventPit, UUID eventUUID, UUID owner) implements DomainEvent {

    public PiggybankCreatedEvent(UUID owner) {
        this(LocalDateTime.now(), UUID.randomUUID(), owner);
    }
}
