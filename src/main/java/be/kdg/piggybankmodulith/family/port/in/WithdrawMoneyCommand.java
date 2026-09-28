package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.common.Money;

import java.util.Objects;
import java.util.UUID;

public record WithdrawMoneyCommand(UUID ownerId, Money amount) {

    public WithdrawMoneyCommand {
        Objects.requireNonNull(ownerId, "Owner ID must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
    }
}
