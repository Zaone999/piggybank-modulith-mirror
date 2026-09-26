package be.kdg.piggybankmodulith.grandparents.adapter.in;

import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositUseCase;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalUseCase;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class FamilyEventListenerAdapter {

    private final ProjectDepositUseCase projectDepositUseCase;
    private final ProjectWithdrawalUseCase projectWithdrawalUseCase;

    FamilyEventListenerAdapter(
            ProjectDepositUseCase projectDepositUseCase,
            ProjectWithdrawalUseCase projectWithdrawalUseCase) {
        this.projectDepositUseCase = projectDepositUseCase;
        this.projectWithdrawalUseCase = projectWithdrawalUseCase;
    }

    @EventListener
    void handleMoneyDepositedEvent(MoneyDepositedEvent event) {
        projectDepositUseCase.projectDeposit(new ProjectDepositCommand(
                event.eventUUID(), event.owner(), event.amount(), event.eventPit()));
    }

    @EventListener
    void handleMoneyWithdrawalEvent(MoneyWithdrawalEvent event) {
        projectWithdrawalUseCase.projectWithdrawal(new ProjectWithdrawalCommand(
                event.eventUUID(), event.owner(), event.amount(), event.eventPit()));
    }
}
