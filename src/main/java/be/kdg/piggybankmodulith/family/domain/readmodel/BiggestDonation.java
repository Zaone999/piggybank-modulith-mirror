package be.kdg.piggybankmodulith.family.domain.readmodel;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Read model: the biggest deposit a piggybank ever received.
 * Shaped for one question: what is the biggest donation the owner received?
 */
public record BiggestDonation(GrandchildId owner, Money amount, LocalDateTime pointInTime) {

    public BiggestDonation {
        Objects.requireNonNull(owner, "Owner must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(pointInTime, "Point in time must not be null");
    }

    public BiggestDonation challengedBy(Money otherAmount, LocalDateTime otherPointInTime) {
        return otherAmount.isLargerThan(amount) ? new BiggestDonation(owner, otherAmount, otherPointInTime) : this;
    }
}
