package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PiggybankCreatedEvent;
import be.kdg.piggybankmodulith.family.domain.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PiggybankTest {

    private final GrandchildId owner = GrandchildId.of(UUID.randomUUID());

    private Piggybank piggybankWith(Money... deposits) {
        List<DomainEvent> history = new ArrayList<>();
        history.add(new PiggybankCreatedEvent(owner.uuid()));
        for (Money deposit : deposits) {
            history.add(new MoneyDepositedEvent(owner.uuid(), deposit));
        }
        return Piggybank.replay(PiggybankId.create(), owner, history);
    }

    @Test
    void createForOwnerRecordsTheCreatedEvent() {
        Piggybank piggybank = Piggybank.createForOwner(owner);

        assertEquals(Money.ZERO, piggybank.getBalance());
        assertTrue(piggybank.getEventStore().isEmpty());
        assertInstanceOf(PiggybankCreatedEvent.class, piggybank.getDomainEvents().getFirst());
    }

    @Test
    void replayingTheHistoryGivesTheBalanceWithoutNewEvents() {
        List<DomainEvent> history = List.of(
                new PiggybankCreatedEvent(owner.uuid()),
                new MoneyDepositedEvent(owner.uuid(), Money.TEN),
                new MoneyDepositedEvent(owner.uuid(), Money.TEN),
                new MoneyWithdrawalEvent(owner.uuid(), new Money(BigDecimal.valueOf(5))),
                new MoneyWithdrawalEvent(owner.uuid(), Money.TEN));

        Piggybank piggybank = Piggybank.replay(PiggybankId.create(), owner, history);

        assertEquals(new Money(BigDecimal.valueOf(5)), piggybank.getBalance());
        assertEquals(5, piggybank.getEventStore().size());
        assertTrue(piggybank.getDomainEvents().isEmpty());
    }

    @Test
    void withdrawLowersTheBalanceAndRecordsAnEvent() {
        Piggybank piggybank = piggybankWith(Money.FIFTY);

        piggybank.withdraw(Money.TEN);

        assertEquals(new Money(BigDecimal.valueOf(40)), piggybank.getBalance());
        assertEquals(1, piggybank.getDomainEvents().size());
        MoneyWithdrawalEvent event = assertInstanceOf(MoneyWithdrawalEvent.class, piggybank.getDomainEvents().getFirst());
        assertEquals(owner.uuid(), event.owner());
        assertEquals(Money.TEN, event.amount());
    }

    @Test
    void withdrawMoreThanTheBalanceIsRefusedWithoutAnEvent() {
        Piggybank piggybank = piggybankWith(Money.TEN);

        assertThrows(InsufficientFundsException.class, () -> piggybank.withdraw(Money.FIFTY));

        assertEquals(Money.TEN, piggybank.getBalance());
        assertTrue(piggybank.getDomainEvents().isEmpty());
    }

    @Test
    void handledEventsBecomePartOfTheHistory() {
        Piggybank piggybank = piggybankWith(Money.TEN);
        piggybank.deposit(Money.TEN);

        piggybank.clearDomainEvents();

        assertTrue(piggybank.getDomainEvents().isEmpty());
        assertEquals(3, piggybank.getEventStore().size());
        assertEquals(new Money(BigDecimal.valueOf(20)), piggybank.getBalance());
    }

    @Test
    void replayingInAWindowOnlySeesTheEventsInThatWindow() {
        List<DomainEvent> history = List.of(
                new PiggybankCreatedEvent(LocalDateTime.of(2025, 12, 1, 10, 0), UUID.randomUUID(), owner.uuid()),
                deposit(LocalDateTime.of(2025, 12, 24, 10, 0), Money.FIFTY),
                deposit(LocalDateTime.of(2026, 1, 15, 10, 0), Money.TEN),
                new MoneyWithdrawalEvent(LocalDateTime.of(2026, 2, 3, 10, 0), UUID.randomUUID(), owner.uuid(),
                        new Money(BigDecimal.valueOf(5))),
                deposit(LocalDateTime.of(2026, 4, 1, 10, 0), Money.TEN));
        EntryWindow firstQuarter = EntryWindow.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 31));

        Piggybank entireHistory = Piggybank.replay(PiggybankId.create(), owner, history);
        Piggybank inWindow = Piggybank.replay(PiggybankId.create(), owner, history, firstQuarter);

        assertEquals(new Money(BigDecimal.valueOf(65)), entireHistory.getBalance());
        assertEquals(new Money(BigDecimal.valueOf(5)), inWindow.getBalance());
        assertEquals(2, inWindow.getEventStore().size());
    }

    @Test
    void theBalanceForAMonthCanBeNegative() {
        List<DomainEvent> history = List.of(
                new PiggybankCreatedEvent(LocalDateTime.of(2026, 1, 1, 10, 0), UUID.randomUUID(), owner.uuid()),
                deposit(LocalDateTime.of(2026, 1, 15, 10, 0), Money.FIFTY),
                new MoneyWithdrawalEvent(LocalDateTime.of(2026, 2, 3, 10, 0), UUID.randomUUID(), owner.uuid(),
                        Money.TEN.add(Money.TEN)),
                deposit(LocalDateTime.of(2026, 2, 20, 10, 0), new Money(BigDecimal.valueOf(5))));

        Piggybank february = Piggybank.replay(PiggybankId.create(), owner, history,
                EntryWindow.month(YearMonth.of(2026, 2)));

        assertEquals(new Money(BigDecimal.valueOf(-15)), february.getBalance());
    }

    @Test
    void aPiggybankReplayedInAWindowIsReadOnly() {
        EntryWindow today = EntryWindow.between(LocalDate.now(), LocalDate.now());
        Piggybank inWindow = Piggybank.replay(PiggybankId.create(), owner,
                List.of(new PiggybankCreatedEvent(owner.uuid()), new MoneyDepositedEvent(owner.uuid(), Money.TEN)), today);

        assertThrows(IllegalStateException.class, () -> inWindow.deposit(Money.TEN));
        assertThrows(IllegalStateException.class, () -> inWindow.withdraw(Money.TEN));
        assertTrue(inWindow.getDomainEvents().isEmpty());
    }

    private MoneyDepositedEvent deposit(LocalDateTime pointInTime, Money amount) {
        return new MoneyDepositedEvent(pointInTime, UUID.randomUUID(), owner.uuid(), amount);
    }
}
