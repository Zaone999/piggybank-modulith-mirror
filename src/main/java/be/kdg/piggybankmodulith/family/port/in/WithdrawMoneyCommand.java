package be.kdg.piggybankmodulith.family.port.in;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawMoneyCommand(UUID owner, BigDecimal amount) {

    public WithdrawMoneyCommand {
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "amount must be greater than zero");
    }
}