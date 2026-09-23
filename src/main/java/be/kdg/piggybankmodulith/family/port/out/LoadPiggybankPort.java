package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.family.domain.OwnerId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;

import java.util.List;
import java.util.Optional;

public interface LoadPiggybankPort {

    Optional<Piggybank> loadBy(OwnerId owner);

    List<Piggybank> loadAll();
}
