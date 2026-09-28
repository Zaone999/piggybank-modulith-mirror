package be.kdg.piggybankmodulith.grandparents.port.in;

import be.kdg.piggybankmodulith.common.Money;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record ProjectDepositCommand(UUID eventId, UUID grandchildId, Money amount, LocalDateTime pointInTime) {

    public ProjectDepositCommand {
        Objects.requireNonNull(eventId, "Event ID must not be null");
        Objects.requireNonNull(grandchildId, "Grandchild ID must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(pointInTime, "Point in time must not be null");
    }
}
