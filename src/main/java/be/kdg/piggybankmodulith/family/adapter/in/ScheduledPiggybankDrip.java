package be.kdg.piggybankmodulith.family.adapter.in;

import be.kdg.piggybankmodulith.family.domain.OwnerId;
import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberNotFoundException;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ScheduledPiggybankDrip {

    private final DepositMoneyUseCase depositMoneyUseCase;

    public ScheduledPiggybankDrip(DepositMoneyUseCase depositMoneyUseCase) {
        this.depositMoneyUseCase = depositMoneyUseCase;
    }

    @Scheduled(fixedRate = 10_000)
    void topUp() throws FamilyMemberNotFoundException {
        DepositMoneyCommand command = new DepositMoneyCommand(OwnerId.BOBBY.uuid(), new BigDecimal("10"));
        this.depositMoneyUseCase.deposit(command);
    }
}
