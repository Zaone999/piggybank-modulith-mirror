package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.OwnerId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.PiggybankId;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;
import be.kdg.piggybankmodulith.family.port.in.WithdrawMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.WithdrawMoneyOutUseCase;
import be.kdg.piggybankmodulith.family.port.out.LoadPiggybankPort;
import be.kdg.piggybankmodulith.family.port.out.UpdatePiggybankPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WithdrawMoneyOutUseCaseImpl implements WithdrawMoneyOutUseCase {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawMoneyOutUseCaseImpl.class);

    private final LoadPiggybankPort loadPiggybankPort;
    private final List<UpdatePiggybankPort> updatePiggybankPorts;

    public WithdrawMoneyOutUseCaseImpl(LoadPiggybankPort loadPiggybankPort, List<UpdatePiggybankPort> updatePiggybankPorts) {
        this.loadPiggybankPort = loadPiggybankPort;
        this.updatePiggybankPorts = updatePiggybankPorts;
    }

    @Override
    @Transactional
    public Piggybank withdraw(WithdrawMoneyCommand command) throws PiggyBankNotFoundException {
        Piggybank piggybank = loadPiggybankPort.loadBy(OwnerId.of(command.owner()))
                .orElseThrow(() -> PiggybankId.notFoundForOwner(OwnerId.of(command.owner())));

        piggybank.withdraw(command.amount());

        logger.info("Taken money out {}", piggybank);
        updatePiggybankPorts.forEach(port -> port.update(piggybank));
        return piggybank;
    }
}
