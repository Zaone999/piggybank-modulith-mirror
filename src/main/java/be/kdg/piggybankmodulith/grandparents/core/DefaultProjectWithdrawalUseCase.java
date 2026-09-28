package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsEntry;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalUseCase;
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
class DefaultProjectWithdrawalUseCase implements ProjectWithdrawalUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProjectWithdrawalUseCase.class);

    private final SavingsEntryLoadPort savingsEntryLoadPort;
    private final SavingsEntryProjectionPort savingsEntryProjectionPort;
    private final SavingsPromiseLoadPort savingsPromiseLoadPort;
    private final GrandchildNameResolver grandchildNameResolver;
    private final List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts;

    DefaultProjectWithdrawalUseCase(
            SavingsEntryLoadPort savingsEntryLoadPort,
            SavingsEntryProjectionPort savingsEntryProjectionPort,
            SavingsPromiseLoadPort savingsPromiseLoadPort,
            GrandchildNameResolver grandchildNameResolver,
            List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts) {
        this.savingsEntryLoadPort = savingsEntryLoadPort;
        this.savingsEntryProjectionPort = savingsEntryProjectionPort;
        this.savingsPromiseLoadPort = savingsPromiseLoadPort;
        this.grandchildNameResolver = grandchildNameResolver;
        this.savingsPromiseUpdatePorts = List.copyOf(savingsPromiseUpdatePorts);
    }

    @Override
    @Transactional
    public void projectWithdrawal(ProjectWithdrawalCommand command) {
        //THIS IS AN EXAMPLE OF AN IDEMPOTENCY CHECK
        //TYPICALLY THIS IS CHECKED IN AN EVENT STORE
        if (savingsEntryLoadPort.existsByEventId(command.eventId())) {
            logger.info("Withdrawal {} already projected, ignored", command.eventId());
            return;
        }

        GrandChildId grandchildId = GrandChildId.of(command.grandchildId());
        savingsEntryProjectionPort.save(SavingsEntry.withdrawal(
                command.eventId(), grandchildId, command.amount(), command.pointInTime()));

        String grandchildName = grandchildNameResolver.nameOf(grandchildId);

        savingsPromiseLoadPort.loadActivePromiseFor(grandchildId)
                .ifPresent(savingsPromise -> {
                    savingsPromise.decreaseBalance(command.amount());
                    savingsPromiseUpdatePorts.forEach(port -> port.update(savingsPromise));

                    logger.info("{} took out {}, promise balance: {} of {}",
                            grandchildName, command.amount(), savingsPromise.getBalance(),
                            savingsPromise.getPromiseBoundary());
                });
    }
}
