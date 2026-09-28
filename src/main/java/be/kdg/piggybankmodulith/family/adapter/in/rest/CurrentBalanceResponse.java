package be.kdg.piggybankmodulith.family.adapter.in.rest;

import java.math.BigDecimal;
import java.util.UUID;

public record CurrentBalanceResponse(UUID ownerId, BigDecimal currentBalance) {
}
