package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
class SavingsPromiseEventPublisherAdapter implements SavingsPromiseUpdatePort {

    private final ApplicationEventPublisher applicationEventPublisher;

    SavingsPromiseEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void update(SavingsPromise savingsPromise) {
        savingsPromise.getDomainEvents().forEach(applicationEventPublisher::publishEvent);

        savingsPromise.clearDomainEvents();
    }
}
