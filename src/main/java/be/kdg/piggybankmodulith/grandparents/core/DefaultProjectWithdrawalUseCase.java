package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalUseCase;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DefaultProjectWithdrawalUseCase implements ProjectWithdrawalUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProjectWithdrawalUseCase.class);

    private final SavingsPromiseLoadPort savingsPromiseLoadPort;
    private final List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts;

    DefaultProjectWithdrawalUseCase(
            SavingsPromiseLoadPort savingsPromiseLoadPort,
            List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts) {
        this.savingsPromiseLoadPort = savingsPromiseLoadPort;
        this.savingsPromiseUpdatePorts = List.copyOf(savingsPromiseUpdatePorts);
    }

    @Override
    public void projectWithdrawal(ProjectWithdrawalCommand command) {
        GrandChildId grandchildId = GrandChildId.of(command.grandchildId());
        savingsPromiseLoadPort.loadActivePromiseFor(grandchildId)
                .ifPresent(savingsPromise -> {
                    savingsPromise.decreaseBalance(command.amount());
                    savingsPromiseUpdatePorts.forEach(port -> port.update(savingsPromise));
                });
    }
}
