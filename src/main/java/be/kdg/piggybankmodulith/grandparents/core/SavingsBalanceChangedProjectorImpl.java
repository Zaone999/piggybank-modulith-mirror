package be.kdg.piggybankmodulith.grandparents.core;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.port.in.SavingsBalanceChangedProjector;
import be.kdg.piggybankmodulith.grandparents.port.in.SavingsDecreaseProjectionCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.SavingsIncreaseProjectionCommand;
import be.kdg.piggybankmodulith.grandparents.port.out.LoadSavingsPromisePort;
import be.kdg.piggybankmodulith.grandparents.port.out.UpdateSavingsPromisePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SavingsBalanceChangedProjectorImpl implements SavingsBalanceChangedProjector {

    private final LoadSavingsPromisePort loadSavingsPromisePort;
    private final List<UpdateSavingsPromisePort> updateSavingsPromisePorts;

    public SavingsBalanceChangedProjectorImpl(LoadSavingsPromisePort loadSavingsPromisePort, List<UpdateSavingsPromisePort> updateSavingsPromisePorts) {
        this.loadSavingsPromisePort = loadSavingsPromisePort;
        this.updateSavingsPromisePorts = updateSavingsPromisePorts;
    }

    @Override
    @Transactional
    public void project(SavingsDecreaseProjectionCommand projectionCommand) {
        SavingsPromise savingsPromise = loadSavingsPromisePort.loadBy(projectionCommand.owner())
                .orElse(SavingsPromise.promise(projectionCommand.owner()));

        savingsPromise.decreaseBalance(projectionCommand.amount());
        updateSavingsPromisePorts.forEach(updateSavingsPromisePort ->
                updateSavingsPromisePort.update(savingsPromise));
        savingsPromise.clearDomainEvents();
    }

    @Override
    @Transactional
    public void project(SavingsIncreaseProjectionCommand projectionCommand) {
        SavingsPromise savingsPromise = loadSavingsPromisePort.loadBy(projectionCommand.owner())
                .orElse(SavingsPromise.promise(projectionCommand.owner()));

        savingsPromise.increaseBalance(projectionCommand.amount());
        updateSavingsPromisePorts.forEach(updateSavingsPromisePort -> updateSavingsPromisePort.update(savingsPromise));
        savingsPromise.clearDomainEvents();
    }
}
