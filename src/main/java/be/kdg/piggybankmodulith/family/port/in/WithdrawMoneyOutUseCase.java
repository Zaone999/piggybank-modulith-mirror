package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberNotFoundException;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;

public interface WithdrawMoneyOutUseCase {

    Piggybank withdraw(WithdrawMoneyCommand command) throws FamilyMemberNotFoundException, PiggyBankNotFoundException;
}
