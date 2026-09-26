package be.kdg.piggybankmodulith.grandparents.adapter.in;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.grandparents.GrandparentsApi;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseUseCase;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class GrandparentsApiAdapter implements GrandparentsApi {

    private final CreateSavingsPromiseUseCase createSavingsPromiseUseCase;

    GrandparentsApiAdapter(CreateSavingsPromiseUseCase createSavingsPromiseUseCase) {
        this.createSavingsPromiseUseCase = createSavingsPromiseUseCase;
    }

    @Override
    public void createSavingsPromise(UUID ownerId, Money promiseBoundary) {
        createSavingsPromiseUseCase.createSavingsPromise(new CreateSavingsPromiseCommand(ownerId, promiseBoundary));
    }
}
