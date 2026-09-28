package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsEntry;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositUseCase;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsEntryLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsEntryProjectionPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class DefaultProjectDepositUseCase implements ProjectDepositUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProjectDepositUseCase.class);

    private final SavingsEntryLoadPort savingsEntryLoadPort;
    private final SavingsEntryProjectionPort savingsEntryProjectionPort;
    private final SavingsPromiseLoadPort savingsPromiseLoadPort;
    private final List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts;

    DefaultProjectDepositUseCase(
            SavingsEntryLoadPort savingsEntryLoadPort,
            SavingsEntryProjectionPort savingsEntryProjectionPort,
            SavingsPromiseLoadPort savingsPromiseLoadPort,
            List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts) {
        this.savingsEntryLoadPort = savingsEntryLoadPort;
        this.savingsEntryProjectionPort = savingsEntryProjectionPort;
        this.savingsPromiseLoadPort = savingsPromiseLoadPort;
        this.savingsPromiseUpdatePorts = List.copyOf(savingsPromiseUpdatePorts);
    }

    @Override
    @Transactional
    public void projectDeposit(ProjectDepositCommand command) {
        // idempotent: an event we already projected has no effect a second time
        if (savingsEntryLoadPort.existsByEventId(command.eventId())) {
            logger.info("Deposit {} already projected, ignored", command.eventId());
            return;
        }

        GrandChildId grandchildId = GrandChildId.of(command.grandchildId());
        savingsEntryProjectionPort.save(SavingsEntry.deposit(
                command.eventId(), grandchildId, command.amount(), command.pointInTime()));

        savingsPromiseLoadPort.loadActivePromiseFor(grandchildId)
                .ifPresent(savingsPromise -> {
                    savingsPromise.increaseBalance(command.amount());
                    savingsPromiseUpdatePorts.forEach(port -> port.update(savingsPromise));
                });
    }
}
