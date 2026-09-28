package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;
import be.kdg.piggybankmodulith.family.port.in.WithdrawMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.WithdrawUseCase;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class DefaultWithdrawUseCase implements WithdrawUseCase {

    private final PiggybankLoadPort piggybankLoadPort;
    private final List<PiggybankUpdatePort> piggybankUpdatePorts;

    DefaultWithdrawUseCase(
            PiggybankLoadPort piggybankLoadPort,
            List<PiggybankUpdatePort> piggybankUpdatePorts) {
        this.piggybankLoadPort = piggybankLoadPort;
        this.piggybankUpdatePorts = List.copyOf(piggybankUpdatePorts);
    }

    @Override
    @Transactional
    public Piggybank withdraw(WithdrawMoneyCommand command) {
        GrandchildId ownerId = GrandchildId.of(command.ownerId());
        Piggybank piggybank = piggybankLoadPort.loadByOwnerId(ownerId)
                .orElseThrow(() -> new PiggyBankNotFoundException(
                        "Piggybank not found for owner " + ownerId.uuid()));

        piggybank.withdraw(command.amount());

        piggybankUpdatePorts.forEach(port -> port.update(piggybank));

        return piggybank;
    }
}
