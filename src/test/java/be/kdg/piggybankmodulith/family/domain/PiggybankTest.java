package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.family.domain.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PiggybankTest {

    private final GrandchildId owner = GrandchildId.of(UUID.randomUUID());

    @Test
    void withdrawLowersTheBalanceAndRecordsAnEvent() {
        Piggybank piggybank = Piggybank.restore(PiggybankId.create(), owner, Money.FIFTY);

        piggybank.withdraw(Money.TEN);

        assertEquals(new Money(BigDecimal.valueOf(40)), piggybank.getBalance());
        assertEquals(1, piggybank.getDomainEvents().size());
        MoneyWithdrawalEvent event = assertInstanceOf(MoneyWithdrawalEvent.class, piggybank.getDomainEvents().getFirst());
        assertEquals(owner.uuid(), event.owner());
        assertEquals(Money.TEN, event.amount());
    }

    @Test
    void withdrawMoreThanTheBalanceIsRefusedWithoutAnEvent() {
        Piggybank piggybank = Piggybank.restore(PiggybankId.create(), owner, Money.TEN);

        assertThrows(InsufficientFundsException.class, () -> piggybank.withdraw(Money.FIFTY));

        assertEquals(Money.TEN, piggybank.getBalance());
        assertTrue(piggybank.getDomainEvents().isEmpty());
    }
}
