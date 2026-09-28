package be.kdg.piggybankmodulith.family.port.in;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The snapshots cover every event of every piggybank before the date.
 */
public record TakeSnapshotsCommand(LocalDateTime date) {

    public TakeSnapshotsCommand {
        Objects.requireNonNull(date, "Date must not be null");
    }
}
