package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankCommand;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class DefaultCreatePiggybankUseCase implements CreatePiggybankUseCase {

    private final List<PiggybankUpdatePort> piggybankUpdatePorts;

    DefaultCreatePiggybankUseCase(List<PiggybankUpdatePort> piggybankUpdatePorts) {
        this.piggybankUpdatePorts = List.copyOf(piggybankUpdatePorts);
    }

    @Override
    public Piggybank createPiggybank(CreatePiggybankCommand command) {
        GrandchildId ownerId = GrandchildId.of(command.ownerId());
        Piggybank piggybank = Piggybank.createForOwner(ownerId);

        piggybankUpdatePorts.forEach(port -> port.update(piggybank));

        return piggybank;
    }
}
