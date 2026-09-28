package be.kdg.piggybankmodulith.family.adapter.in.listeners;

import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.family.domain.readmodel.MovementType;
import be.kdg.piggybankmodulith.family.port.in.ProjectMovementCommand;
import be.kdg.piggybankmodulith.family.port.in.ProjectMovementUseCase;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * CQRS inside one module: Family listens to its own events to keep its read models up to date.
 */
@Component
class StatisticsEventListenerAdapter {

    private final ProjectMovementUseCase projectMovementUseCase;

    StatisticsEventListenerAdapter(ProjectMovementUseCase projectMovementUseCase) {
        this.projectMovementUseCase = projectMovementUseCase;
    }

    @EventListener
    void handleMoneyDepositedEvent(MoneyDepositedEvent event) {
        projectMovementUseCase.projectMovement(new ProjectMovementCommand(
                event.eventUUID(), event.owner(), MovementType.DEPOSIT, event.amount(), event.eventPit()));
    }

    @EventListener
    void handleMoneyWithdrawalEvent(MoneyWithdrawalEvent event) {
        projectMovementUseCase.projectMovement(new ProjectMovementCommand(
                event.eventUUID(), event.owner(), MovementType.WITHDRAWAL, event.amount(), event.eventPit()));
    }
}
