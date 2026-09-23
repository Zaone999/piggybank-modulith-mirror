package be.kdg.piggybankmodulith.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record PromiseLimitBoundaryReachedEvent(LocalDateTime eventPit, UUID eventUUID,
                                               UUID owner) implements DomainEvent {

    public PromiseLimitBoundaryReachedEvent(UUID owner) {
        this(LocalDateTime.now(), UUID.randomUUID(), owner);
    }

    @Override
    public LocalDateTime eventPit() {
        return eventPit;
    }

    @Override
    public UUID eventUUID() {
        return eventUUID;
    }

}
