package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.readmodel.BiggestDonation;
import be.kdg.piggybankmodulith.family.domain.readmodel.MonthlySavings;
import be.kdg.piggybankmodulith.family.port.out.BiggestDonationPort;
import be.kdg.piggybankmodulith.family.port.out.MonthlySavingsPort;
import be.kdg.piggybankmodulith.family.port.out.ProjectedEventPort;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

/**
 * The query side's storage: tables shaped for the questions, separate from the event store.
 */
@Component
class StatisticsPersistenceAdapter implements MonthlySavingsPort, BiggestDonationPort, ProjectedEventPort {

    private final MonthlySavingsJpaRepository monthlySavingsRepository;
    private final BiggestDonationJpaRepository biggestDonationRepository;
    private final ProjectedEventJpaRepository projectedEventRepository;

    StatisticsPersistenceAdapter(MonthlySavingsJpaRepository monthlySavingsRepository,
                                 BiggestDonationJpaRepository biggestDonationRepository,
                                 ProjectedEventJpaRepository projectedEventRepository) {
        this.monthlySavingsRepository = monthlySavingsRepository;
        this.biggestDonationRepository = biggestDonationRepository;
        this.projectedEventRepository = projectedEventRepository;
    }

    @Override
    public Optional<MonthlySavings> load(GrandchildId owner, YearMonth month) {
        return monthlySavingsRepository.findByOwnerAndMonth(owner.uuid(), month.atDay(1)).map(this::toMonthlySavings);
    }

    @Override
    public Optional<MonthlySavings> loadBestMonth(GrandchildId owner) {
        return monthlySavingsRepository.findFirstByOwnerOrderByBalanceDescMonthAsc(owner.uuid()).map(this::toMonthlySavings);
    }

    @Override
    public void save(MonthlySavings monthlySavings) {
        UUID owner = monthlySavings.owner().uuid();
        MonthlySavingsJpaEntity entity = monthlySavingsRepository.findByOwnerAndMonth(owner, monthlySavings.month().atDay(1))
                .orElseGet(() -> new MonthlySavingsJpaEntity(UUID.randomUUID(), owner, monthlySavings.month().atDay(1), null));
        entity.setBalance(monthlySavings.balance().value());
        monthlySavingsRepository.save(entity);
    }

    @Override
    public Optional<BiggestDonation> load(GrandchildId owner) {
        return biggestDonationRepository.findById(owner.uuid())
                .map(entity -> new BiggestDonation(owner, new Money(entity.getAmount()), entity.getPointInTime()));
    }

    @Override
    public void save(BiggestDonation biggestDonation) {
        biggestDonationRepository.save(new BiggestDonationJpaEntity(
                biggestDonation.owner().uuid(), biggestDonation.amount().value(), biggestDonation.pointInTime()));
    }

    @Override
    public boolean isProjected(UUID eventId) {
        return projectedEventRepository.existsById(eventId);
    }

    @Override
    public void markProjected(UUID eventId) {
        projectedEventRepository.save(new ProjectedEventJpaEntity(eventId));
    }

    @Override
    public void deleteAllMonthlySavings() {
        monthlySavingsRepository.deleteAllInBatch();
    }

    @Override
    public void deleteAllBiggestDonations() {
        biggestDonationRepository.deleteAllInBatch();
    }

    @Override
    public void forgetAllProjectedEvents() {
        projectedEventRepository.deleteAllInBatch();
    }

    private MonthlySavings toMonthlySavings(MonthlySavingsJpaEntity entity) {
        return new MonthlySavings(GrandchildId.of(entity.getOwner()), YearMonth.from(entity.getMonth()),
                new Money(entity.getBalance()));
    }
}
