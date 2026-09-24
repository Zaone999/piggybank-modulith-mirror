package be.kdg.piggybankmodulith.family.adapter.in;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.FamilyApi;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankCommand;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositUseCase;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class FamilyApiAdapter implements FamilyApi {

    private final CreatePiggybankUseCase createPiggybankUseCase;
    private final DepositUseCase depositUseCase;

    FamilyApiAdapter(CreatePiggybankUseCase createPiggybankUseCase, DepositUseCase depositUseCase) {
        this.createPiggybankUseCase = createPiggybankUseCase;
        this.depositUseCase = depositUseCase;
    }

    @Override
    public void createPiggybank(UUID ownerId) {
        createPiggybankUseCase.createPiggybank(new CreatePiggybankCommand(ownerId));
    }

    @Override
    public void deposit(UUID ownerId, Money amount) {
        depositUseCase.deposit(new DepositMoneyCommand(ownerId, amount));
    }
}
