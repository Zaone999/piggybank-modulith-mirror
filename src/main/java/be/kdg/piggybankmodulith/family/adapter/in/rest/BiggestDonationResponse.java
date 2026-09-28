package be.kdg.piggybankmodulith.family.adapter.in.rest;

import be.kdg.piggybankmodulith.family.domain.readmodel.BiggestDonation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BiggestDonationResponse(UUID ownerId, BigDecimal amount, LocalDateTime pointInTime) {

    public static BiggestDonationResponse from(BiggestDonation biggestDonation) {
        return new BiggestDonationResponse(biggestDonation.owner().uuid(), biggestDonation.amount().value(),
                biggestDonation.pointInTime());
    }
}
