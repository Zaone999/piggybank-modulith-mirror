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
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Each event reaches Grandparents after Family's transaction has committed, on another thread, in a new
 * transaction of its own: @ApplicationModuleListener is @Async + @Transactional(REQUIRES_NEW) +
 * @TransactionalEventListener. The Event Publication Registry stores every delivery before the commit,
 * so a listener that fails stays on record (FAILED) instead of being lost.
 */
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

    @ApplicationModuleListener
    void handleFamilyMemberBornEvent(FamilyMemberBornEvent event) {
        projectGrandchildUseCase.projectGrandchild(new ProjectGrandchildCommand(
                event.familyMemberId(), event.firstName(), event.lastName()));
    }

    @ApplicationModuleListener
    void handlePiggybankCreatedEvent(PiggybankCreatedEvent event) {
        createSavingsPromiseUseCase.createSavingsPromise(new CreateSavingsPromiseCommand(
                event.owner(), SavingsPromise.DEFAULT_PROMISE_BOUNDARY));
    }

    @ApplicationModuleListener
    void handleMoneyDepositedEvent(MoneyDepositedEvent event) {
        projectDepositUseCase.projectDeposit(new ProjectDepositCommand(
                event.eventUUID(), event.owner(), event.amount(), event.eventPit()));
    }

    @ApplicationModuleListener
    void handleMoneyWithdrawalEvent(MoneyWithdrawalEvent event) {
        projectWithdrawalUseCase.projectWithdrawal(new ProjectWithdrawalCommand(
                event.eventUUID(), event.owner(), event.amount(), event.eventPit()));
    }
}
