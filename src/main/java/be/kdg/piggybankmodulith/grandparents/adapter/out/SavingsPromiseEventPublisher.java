package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.port.out.UpdateSavingsPromisePort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SavingsPromiseEventPublisher implements UpdateSavingsPromisePort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SavingsPromiseEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public SavingsPromise update(SavingsPromise savingsPromise) {
        savingsPromise.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        savingsPromise.clearDomainEvents();
        return savingsPromise;
    }
}
