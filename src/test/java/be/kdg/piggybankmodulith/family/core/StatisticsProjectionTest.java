package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.readmodel.BiggestDonation;
import be.kdg.piggybankmodulith.family.domain.readmodel.MonthlySavings;
import be.kdg.piggybankmodulith.family.domain.readmodel.MovementType;
import be.kdg.piggybankmodulith.family.port.in.ProjectMovementCommand;
import be.kdg.piggybankmodulith.family.port.out.BiggestDonationPort;
import be.kdg.piggybankmodulith.family.port.out.MonthlySavingsPort;
import be.kdg.piggybankmodulith.family.port.out.ProjectedEventPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsProjectionTest {

    private final InMemoryReadModels readModels = new InMemoryReadModels();
    private final StatisticsProjection projection = new StatisticsProjection(readModels, readModels, readModels);
    private final UUID owner = UUID.randomUUID();

    @Test
    void depositsAndWithdrawalsEndUpInTheirMonth() {
        projection.project(deposit(LocalDateTime.of(2026, 1, 10, 10, 0), Money.TEN));
        projection.project(deposit(LocalDateTime.of(2026, 2, 3, 10, 0), Money.FIFTY));
        projection.project(withdrawal(LocalDateTime.of(2026, 2, 20, 10, 0), Money.TEN));

        MonthlySavings best = readModels.loadBestMonth(GrandchildId.of(owner)).orElseThrow();
        assertEquals(YearMonth.of(2026, 2), best.month());
        assertEquals(new Money(BigDecimal.valueOf(40)), best.balance());
    }

    @Test
    void aWithdrawalIsNoDonation() {
        projection.project(deposit(LocalDateTime.of(2026, 1, 10, 10, 0), Money.TEN));
        projection.project(withdrawal(LocalDateTime.of(2026, 1, 11, 10, 0), Money.FIFTY));

        assertEquals(Money.TEN, readModels.load(GrandchildId.of(owner)).orElseThrow().amount());
    }

    @Test
    void anEventDeliveredTwiceCountsOnce() {
        ProjectMovementCommand deposit = deposit(LocalDateTime.of(2026, 1, 10, 10, 0), Money.TEN);

        projection.project(deposit);
        projection.project(deposit);

        assertEquals(Money.TEN, readModels.load(GrandchildId.of(owner), YearMonth.of(2026, 1)).orElseThrow().balance());
    }

    private ProjectMovementCommand deposit(LocalDateTime pointInTime, Money amount) {
        return new ProjectMovementCommand(UUID.randomUUID(), owner, MovementType.DEPOSIT, amount, pointInTime);
    }

    private ProjectMovementCommand withdrawal(LocalDateTime pointInTime, Money amount) {
        return new ProjectMovementCommand(UUID.randomUUID(), owner, MovementType.WITHDRAWAL, amount, pointInTime);
    }

    private static class InMemoryReadModels implements MonthlySavingsPort, BiggestDonationPort, ProjectedEventPort {

        private final Map<String, MonthlySavings> months = new HashMap<>();
        private final Map<GrandchildId, BiggestDonation> donations = new HashMap<>();
        private final Set<UUID> projected = new HashSet<>();

        @Override
        public Optional<MonthlySavings> load(GrandchildId owner, YearMonth month) {
            return Optional.ofNullable(months.get(owner.uuid() + "/" + month));
        }

        @Override
        public Optional<MonthlySavings> loadBestMonth(GrandchildId owner) {
            return months.values().stream()
                    .filter(month -> month.owner().equals(owner))
                    .max(Comparator.comparing(month -> month.balance().value()));
        }

        @Override
        public void save(MonthlySavings monthlySavings) {
            months.put(monthlySavings.owner().uuid() + "/" + monthlySavings.month(), monthlySavings);
        }

        @Override
        public void deleteAllMonthlySavings() {
            months.clear();
        }

        @Override
        public Optional<BiggestDonation> load(GrandchildId owner) {
            return Optional.ofNullable(donations.get(owner));
        }

        @Override
        public void save(BiggestDonation biggestDonation) {
            donations.put(biggestDonation.owner(), biggestDonation);
        }

        @Override
        public void deleteAllBiggestDonations() {
            donations.clear();
        }

        @Override
        public boolean isProjected(UUID eventId) {
            return projected.contains(eventId);
        }

        @Override
        public void markProjected(UUID eventId) {
            projected.add(eventId);
        }

        @Override
        public void forgetAllProjectedEvents() {
            projected.clear();
        }
    }
}
