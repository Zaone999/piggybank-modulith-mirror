package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.readmodel.BiggestDonation;

import java.util.Optional;
import java.util.UUID;

public interface BiggestDonationQuery {

    /**
     * Reads the read model only: the piggybank itself is never loaded. Empty while nothing was deposited.
     */
    Optional<BiggestDonation> biggestDonation(UUID ownerId);
}
