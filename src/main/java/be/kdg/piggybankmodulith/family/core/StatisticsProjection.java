package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.readmodel.BiggestDonation;
import be.kdg.piggybankmodulith.family.domain.readmodel.MonthlySavings;
import be.kdg.piggybankmodulith.family.domain.readmodel.MovementType;
import be.kdg.piggybankmodulith.family.port.in.ProjectMovementCommand;
import be.kdg.piggybankmodulith.family.port.out.BiggestDonationPort;
import be.kdg.piggybankmodulith.family.port.out.MonthlySavingsPort;
import be.kdg.piggybankmodulith.family.port.out.ProjectedEventPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * Not a use case and not an input port: the projecting both use cases share,
 * when an event happens and when the read models are rebuilt.
 */
@Component
class StatisticsProjection {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsProjection.class);

    private final MonthlySavingsPort monthlySavingsPort;
    private final BiggestDonationPort biggestDonationPort;
    private final ProjectedEventPort projectedEventPort;

    StatisticsProjection(MonthlySavingsPort monthlySavingsPort, BiggestDonationPort biggestDonationPort,
                         ProjectedEventPort projectedEventPort) {
        this.monthlySavingsPort = monthlySavingsPort;
        this.biggestDonationPort = biggestDonationPort;
        this.projectedEventPort = projectedEventPort;
    }

    void project(ProjectMovementCommand command) {
        if (projectedEventPort.isProjected(command.eventId())) {
            logger.info("Event {} already projected, ignored", command.eventId());
            return;
        }

        GrandchildId owner = GrandchildId.of(command.ownerId());
        YearMonth month = YearMonth.from(command.pointInTime());

        MonthlySavings monthlySavings = monthlySavingsPort.load(owner, month)
                .orElseGet(() -> MonthlySavings.nothingYet(owner, month));
        monthlySavingsPort.save(monthlySavings.with(command.type(), command.amount()));

        if (command.type() == MovementType.DEPOSIT) {
            biggestDonationPort.save(biggestDonationPort.load(owner)
                    .map(biggest -> biggest.challengedBy(command.amount(), command.pointInTime()))
                    .orElseGet(() -> new BiggestDonation(owner, command.amount(), command.pointInTime())));
        }

        projectedEventPort.markProjected(command.eventId());
    }
}
