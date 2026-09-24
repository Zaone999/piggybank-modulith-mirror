package be.kdg.piggybankmodulith.grandparents.application;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.grandparents.GrandparentsApi;
import be.kdg.piggybankmodulith.grandparents.domain.GrandChildId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
class GrandparentsService implements GrandparentsApi {

    private final List<SavingsPromise> savingsPromises = new ArrayList<>();

    private final ApplicationEventPublisher applicationEventPublisher;

    GrandparentsService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void createSavingsPromise(UUID ownerId, Money promiseBoundary) {
        GrandChildId grandChildId = GrandChildId.of(ownerId);
        SavingsPromise savingsPromise = SavingsPromise.promise(grandChildId, promiseBoundary);
        this.savingsPromises.add(savingsPromise);
    }

    @EventListener
    private void handleMoneyDepositedEvent(MoneyDepositedEvent event) {
        this.savingsPromises.stream()
                .filter(sp -> sp.isMadeTo(event.owner()))
                .filter(SavingsPromise::notYetTriggered)
                .findFirst()
                .ifPresent(savingsPromise -> {
                    savingsPromise.increaseBalance(event.amount());
                    handleDomainEvents(savingsPromise);
                });
    }

    private void handleDomainEvents(SavingsPromise savingsPromise) {
        savingsPromise.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        savingsPromise.clearDomainEvents();
    }
}
