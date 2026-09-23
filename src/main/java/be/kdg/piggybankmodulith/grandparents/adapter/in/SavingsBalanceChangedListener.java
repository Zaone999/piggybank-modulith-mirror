package be.kdg.piggybankmodulith.grandparents.adapter.in;

import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.grandparents.domain.GrandchildId;
import be.kdg.piggybankmodulith.grandparents.port.in.SavingsDecreaseProjectionCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.SavingsBalanceChangedProjector;
import be.kdg.piggybankmodulith.grandparents.port.in.SavingsIncreaseProjectionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class SavingsBalanceChangedListener {

    private final Logger logger = LoggerFactory.getLogger(SavingsBalanceChangedListener.class);
    private final SavingsBalanceChangedProjector savingsBalanceChangedProjector;

    public SavingsBalanceChangedListener(SavingsBalanceChangedProjector savingsBalanceChangedProjector) {
        this.savingsBalanceChangedProjector = savingsBalanceChangedProjector;
    }

    @ApplicationModuleListener
    public void balanceChanged(MoneyDepositedEvent moneyDepositedEvent) {
        logger.info("Money deposited in piggybank event received {}", moneyDepositedEvent);
        savingsBalanceChangedProjector.project(new SavingsIncreaseProjectionCommand(GrandchildId.of(moneyDepositedEvent.owner()), moneyDepositedEvent.amount()));
    }


    @ApplicationModuleListener
    public void balanceChanged(MoneyWithdrawalEvent moneyWithdrawalEvent) {
        logger.info("Money withdrawn out piggybank event received {}", moneyWithdrawalEvent);
        savingsBalanceChangedProjector.project(new SavingsDecreaseProjectionCommand(GrandchildId.of(moneyWithdrawalEvent.owner()),  moneyWithdrawalEvent.amount()));
    }
}
