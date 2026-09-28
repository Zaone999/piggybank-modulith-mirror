package be.kdg.piggybankmodulith.grandparents.port.in;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;

public interface CreateSavingsPromiseUseCase {

    SavingsPromise createSavingsPromise(CreateSavingsPromiseCommand command);
}
