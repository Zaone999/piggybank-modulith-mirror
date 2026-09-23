package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.OwnerId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberNotFoundException;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyUseCase;
import be.kdg.piggybankmodulith.family.port.out.LoadPiggybankPort;
import be.kdg.piggybankmodulith.family.port.out.UpdatePiggybankPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepositMoneyUseCaseImpl implements DepositMoneyUseCase {

    private final Logger logger = LoggerFactory.getLogger(DepositMoneyUseCaseImpl.class);

    private final LoadPiggybankPort loadPiggybankPort;
    private final List<UpdatePiggybankPort> updatePiggybankPorts;

    public DepositMoneyUseCaseImpl(LoadPiggybankPort loadPiggybankPort, List<UpdatePiggybankPort> updatePiggybankPorts) {
        this.loadPiggybankPort = loadPiggybankPort;
        this.updatePiggybankPorts = updatePiggybankPorts;
    }

    @Override
    @Transactional
    public Piggybank deposit(DepositMoneyCommand command) throws FamilyMemberNotFoundException {
        //you can check if the owner exists.
        OwnerId ownerId = OwnerId.of(command.owner());
        if (!ownerId.equals(OwnerId.BOBBY)) {
            ownerId.notFound();
        }

        Piggybank piggybank = this.loadPiggybankPort.loadBy(ownerId)
                .orElseGet(() -> Piggybank.createForOwner(ownerId));

        piggybank.deposit(command.amount());
        this.updatePiggybankPorts.forEach(port -> port.update(piggybank));
        logger.info("Deposited{}", piggybank);
        return piggybank;
    }
}
