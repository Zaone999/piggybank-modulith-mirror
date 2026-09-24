package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
class SpringApplicationEventPublisherAdapter implements PiggybankUpdatePort {

    private final ApplicationEventPublisher applicationEventPublisher;

    SpringApplicationEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void update(Piggybank piggybank) {
        piggybank.getDomainEvents().forEach(applicationEventPublisher::publishEvent);

        piggybank.clearDomainEvents();
    }
}
