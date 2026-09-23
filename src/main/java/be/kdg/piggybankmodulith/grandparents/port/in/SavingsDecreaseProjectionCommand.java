package be.kdg.piggybankmodulith.grandparents.port.in;

import be.kdg.piggybankmodulith.grandparents.domain.OwnerId;

import java.math.BigDecimal;

public record SavingsDecreaseProjectionCommand(OwnerId owner, BigDecimal amount) {
}
