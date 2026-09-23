package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.common.Money;
import org.springframework.util.Assert;

import java.util.UUID;

public record WithdrawMoneyCommand(UUID owner, Money amount) {

    public WithdrawMoneyCommand {
        Assert.isTrue(amount.compareTo(Money.ZERO) > 0, "amount must be greater than zero");
    }
}