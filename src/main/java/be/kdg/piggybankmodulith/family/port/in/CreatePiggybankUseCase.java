package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.Piggybank;

public interface CreatePiggybankUseCase {

    Piggybank createPiggybank(CreatePiggybankCommand command);
}
