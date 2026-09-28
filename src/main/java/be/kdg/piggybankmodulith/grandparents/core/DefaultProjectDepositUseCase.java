package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositUseCase;
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

    private final SavingsPromiseLoadPort savingsPromiseLoadPort;
    private final List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts;

    DefaultProjectDepositUseCase(
            SavingsPromiseLoadPort savingsPromiseLoadPort,
            List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts) {
        this.savingsPromiseLoadPort = savingsPromiseLoadPort;
        this.savingsPromiseUpdatePorts = savingsPromiseUpdatePorts;
    }

    @Override
    @Transactional
    public void projectDeposit(ProjectDepositCommand command) {
        GrandChildId grandchildId = GrandChildId.of(command.grandchildId());
        savingsPromiseLoadPort.loadActivePromiseFor(grandchildId)
                .ifPresent(savingsPromise -> {
                    savingsPromise.increaseBalance(command.amount());
                    savingsPromiseUpdatePorts.forEach(port -> port.update(savingsPromise));
                });
    }
}
