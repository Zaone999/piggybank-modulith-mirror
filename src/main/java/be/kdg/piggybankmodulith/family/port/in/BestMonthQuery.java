package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.readmodel.MonthlySavings;

import java.util.Optional;
import java.util.UUID;

public interface BestMonthQuery {

    /**
     * Reads the read model only: the piggybank itself is never loaded. Empty while nothing was saved.
     */
    Optional<MonthlySavings> bestMonth(UUID ownerId);
}
