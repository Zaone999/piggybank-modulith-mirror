package be.kdg.piggybankmodulith.grandparents.adapter.in;

import be.kdg.piggybankmodulith.common.events.FamilyMemberBornEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PiggybankCreatedEvent;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.CreateSavingsPromiseUseCase;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectDepositUseCase;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectGrandchildCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectGrandchildUseCase;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalCommand;
import be.kdg.piggybankmodulith.grandparents.port.in.ProjectWithdrawalUseCase;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class FamilyEventListenerAdapter {

    private final ProjectGrandchildUseCase projectGrandchildUseCase;
    private final CreateSavingsPromiseUseCase createSavingsPromiseUseCase;
    private final ProjectDepositUseCase projectDepositUseCase;
    private final ProjectWithdrawalUseCase projectWithdrawalUseCase;

    FamilyEventListenerAdapter(
            ProjectGrandchildUseCase projectGrandchildUseCase,
            CreateSavingsPromiseUseCase createSavingsPromiseUseCase,
            ProjectDepositUseCase projectDepositUseCase,
            ProjectWithdrawalUseCase projectWithdrawalUseCase) {
        this.projectGrandchildUseCase = projectGrandchildUseCase;
        this.createSavingsPromiseUseCase = createSavingsPromiseUseCase;
        this.projectDepositUseCase = projectDepositUseCase;
        this.projectWithdrawalUseCase = projectWithdrawalUseCase;
    }

    @EventListener
    void handleFamilyMemberBornEvent(FamilyMemberBornEvent event) {
        projectGrandchildUseCase.projectGrandchild(new ProjectGrandchildCommand(
                event.familyMemberId(), event.firstName(), event.lastName()));
    }

    @EventListener
    void handlePiggybankCreatedEvent(PiggybankCreatedEvent event) {
        createSavingsPromiseUseCase.createSavingsPromise(new CreateSavingsPromiseCommand(
                event.owner(), SavingsPromise.DEFAULT_PROMISE_BOUNDARY));
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
