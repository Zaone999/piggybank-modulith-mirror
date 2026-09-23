package be.kdg.piggybankmodulith.grandparents.port.in;

import be.kdg.piggybankmodulith.grandparents.domain.OwnerId;

import java.math.BigDecimal;

public record SavingsIncreaseProjectionCommand(OwnerId owner, BigDecimal amount) {
}
