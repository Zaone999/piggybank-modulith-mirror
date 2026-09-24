package be.kdg.piggybankmodulith.grandparents;

import be.kdg.piggybankmodulith.common.Money;

import java.util.UUID;

public interface GrandparentsApi {

    void createSavingsPromise(UUID ownerId, Money promiseBoundary);
}
