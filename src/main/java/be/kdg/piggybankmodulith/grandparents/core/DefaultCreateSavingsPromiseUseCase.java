package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseUseCase;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DefaultCreateSavingsPromiseUseCase implements CreateSavingsPromiseUseCase {

    private final List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts;

    DefaultCreateSavingsPromiseUseCase(List<SavingsPromiseUpdatePort> savingsPromiseUpdatePorts) {
        this.savingsPromiseUpdatePorts = List.copyOf(savingsPromiseUpdatePorts);
    }

    @Override
    public SavingsPromise createSavingsPromise(CreateSavingsPromiseCommand command) {
        SavingsPromise savingsPromise = SavingsPromise.promise(
                GrandChildId.of(command.grandchildId()), command.promiseBoundary());

        savingsPromiseUpdatePorts.forEach(port -> port.update(savingsPromise));

        return savingsPromise;
    }
}
