package be.kdg.piggybankmodulith.grandparents.port.out;

import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;

import java.util.Optional;

public interface SavingsPromiseLoadPort {

    Optional<SavingsPromise> loadActivePromiseFor(GrandChildId grandchildId);
}
