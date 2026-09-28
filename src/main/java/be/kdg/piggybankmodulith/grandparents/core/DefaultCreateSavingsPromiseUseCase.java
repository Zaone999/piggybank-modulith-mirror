package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.domain.exceptions.SavingsPromiseAlreadyExistsException;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseUseCase;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseLoadPort;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class DefaultCreateSavingsPromiseUseCase implements CreateSavingsPromiseUseCase {

    private final SavingsPromiseLoadPort savingsPromiseLoadPort;
    private final List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts;

    DefaultCreateSavingsPromiseUseCase(
            SavingsPromiseLoadPort savingsPromiseLoadPort,
            List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts) {
        this.savingsPromiseLoadPort = savingsPromiseLoadPort;
        this.savingsPromiseUpdatePorts = List.copyOf(savingsPromiseUpdatePorts);
    }

    @Override
    @Transactional
    public SavingsPromise createSavingsPromise(CreateSavingsPromiseCommand command) {
        GrandChildId grandchildId = GrandChildId.of(command.grandchildId());

        if (savingsPromiseLoadPort.loadActivePromiseFor(grandchildId).isPresent()) {
            throw new SavingsPromiseAlreadyExistsException(
                    "Active savings promise already exists for grandchild " + command.grandchildId());
        }

        SavingsPromise savingsPromise = SavingsPromise.promise(grandchildId, command.promiseBoundary());

        savingsPromiseUpdatePorts.forEach(port -> port.update(savingsPromise));

        return savingsPromise;
    }
}
