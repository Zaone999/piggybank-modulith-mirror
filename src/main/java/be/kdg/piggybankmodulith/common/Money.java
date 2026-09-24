package be.kdg.piggybankmodulith.common;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal value) {

    public static final Money ZERO = new Money(BigDecimal.ZERO);
    public static final Money TEN = new Money(BigDecimal.TEN);
    public static final Money FIFTY = new Money(BigDecimal.valueOf(50L));

    public Money {
        Objects.requireNonNull(value, "Amount must not be null");

        if (value.signum() < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }

        value = value.stripTrailingZeros();
    }

    public Money subtract(Money subtrahend) {
        return new Money(value.subtract(subtrahend.value));
    }

    public Money add(Money augend) {
        return new Money(value.add(augend.value));
    }

    public int compareTo(Money other) {
        return value.compareTo(other.value);
    }

    public boolean isLessThan(Money other) {
        return compareTo(other) < 0;
    }
}
