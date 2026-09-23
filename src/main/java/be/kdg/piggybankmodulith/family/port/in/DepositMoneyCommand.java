package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.common.Money;
import org.springframework.util.Assert;

import java.util.UUID;

public record DepositMoneyCommand(UUID owner, Money amount) {

    public DepositMoneyCommand {
        Assert.isTrue(amount.compareTo(Money.ZERO) > 0, "amount must be greater than zero");
    }
}
