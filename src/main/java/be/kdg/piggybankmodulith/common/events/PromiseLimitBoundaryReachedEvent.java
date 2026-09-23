package be.kdg.piggybankmodulith.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record PromiseLimitBoundaryReachedEvent(LocalDateTime eventPit, UUID eventUUID,
                                               UUID grandchild) implements DomainEvent {

    public PromiseLimitBoundaryReachedEvent(UUID grandchild) {
        this(LocalDateTime.now(), UUID.randomUUID(), grandchild);
    }

}
