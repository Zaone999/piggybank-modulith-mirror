package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.readmodel.MovementType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record ProjectMovementCommand(UUID eventId, UUID ownerId, MovementType type, Money amount,
                                     LocalDateTime pointInTime) {

    public ProjectMovementCommand {
        Objects.requireNonNull(eventId, "Event ID must not be null");
        Objects.requireNonNull(ownerId, "Owner ID must not be null");
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(pointInTime, "Point in time must not be null");
    }
}
