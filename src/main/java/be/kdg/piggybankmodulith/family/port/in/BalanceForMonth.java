package be.kdg.piggybankmodulith.family.port.in;

import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

public record BalanceForMonth(UUID ownerId, YearMonth month) {

    public BalanceForMonth {
        Objects.requireNonNull(ownerId, "Owner ID must not be null");
        Objects.requireNonNull(month, "Month must not be null");
    }
}
