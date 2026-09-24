package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.PiggybankId;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositUseCase;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DefaultDepositUseCase implements DepositUseCase {

    private final PiggybankLoadPort piggybankLoadPort;
    private final List<PiggybankUpdatePort> piggybankUpdatePorts;

    DefaultDepositUseCase(
            PiggybankLoadPort piggybankLoadPort,
            List<PiggybankUpdatePort> piggybankUpdatePorts) {
        this.piggybankLoadPort = piggybankLoadPort;
        this.piggybankUpdatePorts = List.copyOf(piggybankUpdatePorts);
    }

    @Override
    public Piggybank deposit(DepositMoneyCommand command) {
        GrandchildId ownerId = GrandchildId.of(command.ownerId());
        Piggybank piggybank = piggybankLoadPort.loadByOwnerId(ownerId)
                .orElseThrow(() -> PiggybankId.notFoundForOwner(ownerId));

        piggybank.deposit(command.amount());

        piggybankUpdatePorts.forEach(port -> port.update(piggybank));

        return piggybank;
    }
}
