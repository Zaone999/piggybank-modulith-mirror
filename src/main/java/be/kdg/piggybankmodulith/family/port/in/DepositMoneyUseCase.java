package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberNotFoundException;

public interface DepositMoneyUseCase {

    Piggybank deposit(DepositMoneyCommand command) throws FamilyMemberNotFoundException;
}
