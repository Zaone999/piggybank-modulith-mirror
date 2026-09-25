package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankAlreadyExistsException;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankCommand;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DefaultCreatePiggybankUseCase implements CreatePiggybankUseCase {

    private final PiggybankLoadPort piggybankLoadPort;
    private final List<PiggybankUpdatePort> piggybankUpdatePorts;

    DefaultCreatePiggybankUseCase(
            PiggybankLoadPort piggybankLoadPort,
            List<PiggybankUpdatePort> piggybankUpdatePorts) {
        this.piggybankLoadPort = piggybankLoadPort;
        this.piggybankUpdatePorts = List.copyOf(piggybankUpdatePorts);
    }

    @Override
    public Piggybank createPiggybank(CreatePiggybankCommand command) {
        GrandchildId ownerId = GrandchildId.of(command.ownerId());

        if (piggybankLoadPort.loadByOwnerId(ownerId).isPresent()) {
            throw new PiggyBankAlreadyExistsException(
                    "Piggybank already exists for owner " + command.ownerId());
        }

        Piggybank piggybank = Piggybank.createForOwner(ownerId);

        piggybankUpdatePorts.forEach(port -> port.update(piggybank));

        return piggybank;
    }
}
