package be.kdg.piggybankmodulith.grandparents.port.out;

import be.kdg.piggybankmodulith.grandparents.domain.GrandchildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;

import java.util.List;
import java.util.Optional;

public interface LoadSavingsPromisePort {

    Optional<SavingsPromise> loadBy(GrandchildId owner);

}