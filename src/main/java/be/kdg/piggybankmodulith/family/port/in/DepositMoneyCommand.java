package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.common.Money;

import java.util.Objects;
import java.util.UUID;

public record DepositMoneyCommand(UUID ownerId, Money amount) {

    public DepositMoneyCommand {
        Objects.requireNonNull(ownerId, "Owner ID must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
    }
}
