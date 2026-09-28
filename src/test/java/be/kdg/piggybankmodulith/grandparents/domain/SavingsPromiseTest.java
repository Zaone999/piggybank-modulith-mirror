package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingsPromiseTest {

    private final GrandChildId grandchild = GrandChildId.of(UUID.randomUUID());

    @Test
    void withdrawalLowersTheSavedBalance() {
        SavingsPromise promise = SavingsPromise.promise(grandchild, Money.FIFTY);
        promise.increaseBalance(Money.TEN);
        promise.increaseBalance(Money.TEN);

        promise.decreaseBalance(new Money(BigDecimal.valueOf(5)));

        assertEquals(new Money(BigDecimal.valueOf(15)), promise.getBalance());
    }

    @Test
    void withdrawalOfMoneySavedBeforeThePromiseStopsAtZero() {
        SavingsPromise promise = SavingsPromise.promise(grandchild, Money.FIFTY);
        promise.increaseBalance(Money.TEN);

        promise.decreaseBalance(Money.FIFTY);

        assertEquals(Money.ZERO, promise.getBalance());
    }
}
