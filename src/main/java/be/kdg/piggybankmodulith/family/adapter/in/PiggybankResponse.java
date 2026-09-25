package be.kdg.piggybankmodulith.family.adapter.in;

import be.kdg.piggybankmodulith.family.domain.Piggybank;

import java.math.BigDecimal;
import java.util.UUID;

public record PiggybankResponse(UUID piggybankId, UUID ownerId, BigDecimal balance) {

    public static PiggybankResponse from(Piggybank piggybank) {
        return new PiggybankResponse(
                piggybank.getPiggybankId().uuid(),
                piggybank.getOwner().uuid(),
                piggybank.getBalance().value());
    }
}
