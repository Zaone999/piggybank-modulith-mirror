package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.family.domain.readmodel.MovementType;
import be.kdg.piggybankmodulith.family.port.in.ProjectMovementCommand;
import be.kdg.piggybankmodulith.family.port.in.RebuildStatisticsUseCase;
import be.kdg.piggybankmodulith.family.port.out.BiggestDonationPort;
import be.kdg.piggybankmodulith.family.port.out.MonthlySavingsPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankHistoryPort;
import be.kdg.piggybankmodulith.family.port.out.ProjectedEventPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class DefaultRebuildStatisticsUseCase implements RebuildStatisticsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRebuildStatisticsUseCase.class);

    private final PiggybankHistoryPort piggybankHistoryPort;
    private final MonthlySavingsPort monthlySavingsPort;
    private final BiggestDonationPort biggestDonationPort;
    private final ProjectedEventPort projectedEventPort;
    private final StatisticsProjection statisticsProjection;

    DefaultRebuildStatisticsUseCase(PiggybankHistoryPort piggybankHistoryPort, MonthlySavingsPort monthlySavingsPort,
                                    BiggestDonationPort biggestDonationPort, ProjectedEventPort projectedEventPort,
                                    StatisticsProjection statisticsProjection) {
        this.piggybankHistoryPort = piggybankHistoryPort;
        this.monthlySavingsPort = monthlySavingsPort;
        this.biggestDonationPort = biggestDonationPort;
        this.projectedEventPort = projectedEventPort;
        this.statisticsProjection = statisticsProjection;
    }

    @Override
    @Transactional
    public void rebuildStatistics() {
        monthlySavingsPort.deleteAllMonthlySavings();
        biggestDonationPort.deleteAllBiggestDonations();
        projectedEventPort.forgetAllProjectedEvents();

        int replayed = 0;
        for (var event : piggybankHistoryPort.loadAllEvents()) {
            switch (event) {
                case MoneyDepositedEvent deposited -> statisticsProjection.project(new ProjectMovementCommand(
                        deposited.eventUUID(), deposited.owner(), MovementType.DEPOSIT,
                        deposited.amount(), deposited.eventPit()));
                case MoneyWithdrawalEvent withdrawn -> statisticsProjection.project(new ProjectMovementCommand(
                        withdrawn.eventUUID(), withdrawn.owner(), MovementType.WITHDRAWAL,
                        withdrawn.amount(), withdrawn.eventPit()));
                default -> {
                    continue; //ignore
                }
            }
            replayed++;
        }
        logger.info("Statistics rebuilt from {} stored events", replayed);
    }
}
