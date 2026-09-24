package be.kdg.piggybankmodulith.common;

import java.math.BigDecimal;


/**
 * This is a simple example, there exists a MONEY API in java (
 *
 *
 */
public class Money {

    private final BigDecimal value;


    public static final Money ZERO = new Money(BigDecimal.ZERO);
    public static final Money TEN = new Money(BigDecimal.TEN);
    public static final Money FIFTY = new Money(BigDecimal.valueOf(50L));

    private Money(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0L) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        this.value = amount;
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


    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}
