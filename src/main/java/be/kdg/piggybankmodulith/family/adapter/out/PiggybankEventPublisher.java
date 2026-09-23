package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.out.UpdatePiggybankPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PiggybankEventPublisher implements UpdatePiggybankPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public PiggybankEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Piggybank update(Piggybank piggybank) {
        piggybank.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        piggybank.clearDomainEvents();
        return piggybank;
    }
}
