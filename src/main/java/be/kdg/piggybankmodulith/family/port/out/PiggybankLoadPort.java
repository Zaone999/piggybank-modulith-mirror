package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;

import java.util.Optional;

public interface PiggybankLoadPort {

    Optional<Piggybank> loadByOwnerId(GrandchildId ownerId);
}
