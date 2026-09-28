package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.EntryWindow;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;
import be.kdg.piggybankmodulith.family.port.in.BalanceForMonth;
import be.kdg.piggybankmodulith.family.port.in.PiggybankBalanceQuery;
import be.kdg.piggybankmodulith.family.port.out.PiggybankLoadPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
class DefaultPiggybankBalanceQuery implements PiggybankBalanceQuery {

    private final PiggybankLoadPort piggybankLoadPort;

    DefaultPiggybankBalanceQuery(PiggybankLoadPort piggybankLoadPort) {
        this.piggybankLoadPort = piggybankLoadPort;
    }

    @Override
    @Transactional(readOnly = true)
    public Money currentBalance(UUID ownerId) {
        return replay(ownerId, EntryWindow.all()).getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public Money balanceForMonth(BalanceForMonth query) {
        return replay(query.ownerId(), EntryWindow.month(query.month())).getBalance();
    }

    private Piggybank replay(UUID ownerId, EntryWindow window) {
        return piggybankLoadPort.loadByOwnerId(GrandchildId.of(ownerId), window)
                .orElseThrow(() -> new PiggyBankNotFoundException("Piggybank not found for owner " + ownerId));
    }
}
