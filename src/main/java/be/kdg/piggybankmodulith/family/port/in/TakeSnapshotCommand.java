package be.kdg.piggybankmodulith.family.port.in;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The snapshot covers every event of the owner's piggybank before the date.
 */
public record TakeSnapshotCommand(UUID ownerId, LocalDateTime date) {

    public TakeSnapshotCommand {
        Objects.requireNonNull(ownerId, "Owner ID must not be null");
        Objects.requireNonNull(date, "Date must not be null");
    }
}
