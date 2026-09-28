package be.kdg.piggybankmodulith.family.domain.readmodel;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;

import java.time.YearMonth;
import java.util.Objects;

/**
 * Read model: what was saved in one month, deposits minus withdrawals, so it can be negative.
 * Shaped for one question: what is the best month to be the owner?
 */
public record MonthlySavings(GrandchildId owner, YearMonth month, Money balance) {

    public MonthlySavings {
        Objects.requireNonNull(owner, "Owner must not be null");
        Objects.requireNonNull(month, "Month must not be null");
        Objects.requireNonNull(balance, "Balance must not be null");
    }

    public static MonthlySavings nothingYet(GrandchildId owner, YearMonth month) {
        return new MonthlySavings(owner, month, Money.ZERO);
    }

    public MonthlySavings with(MovementType type, Money amount) {
        return new MonthlySavings(owner, month, switch (type) {
            case DEPOSIT -> balance.add(amount);
            case WITHDRAWAL -> balance.subtract(amount);
        });
    }
}
