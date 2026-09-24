package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.Piggybank;

public interface DepositUseCase {

    Piggybank deposit(DepositMoneyCommand command);
}
