package be.kdg.piggybankmodulith.family.domain.readmodel;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadModelTest {

    private final GrandchildId owner = GrandchildId.of(UUID.randomUUID());

    @Test
    void aMonthCanEndNegative() {
        MonthlySavings february = MonthlySavings.nothingYet(owner, YearMonth.of(2026, 2))
                .with(MovementType.DEPOSIT, new Money(BigDecimal.valueOf(5)))
                .with(MovementType.WITHDRAWAL, Money.TEN.add(Money.TEN));

        assertEquals(new Money(BigDecimal.valueOf(-15)), february.balance());
    }

    @Test
    void theBiggestDonationKeepsTheFirstOnATie() {
        LocalDateTime first = LocalDateTime.of(2026, 1, 1, 10, 0);
        BiggestDonation biggest = new BiggestDonation(owner, Money.TEN, first)
                .challengedBy(new Money(BigDecimal.valueOf(5)), first.plusDays(1))
                .challengedBy(Money.TEN, first.plusDays(2));

        assertEquals(Money.TEN, biggest.amount());
        assertEquals(first, biggest.pointInTime());
        assertEquals(Money.FIFTY, biggest.challengedBy(Money.FIFTY, first.plusDays(3)).amount());
    }
}
