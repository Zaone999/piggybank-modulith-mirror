package be.kdg.piggybankmodulith.family.application;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.family.FamilyApi;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
class FamilyService implements FamilyApi {

    private static final Logger log = LoggerFactory.getLogger(FamilyService.class);

    private final List<Piggybank> piggybanks = new ArrayList<>();

    private final ApplicationEventPublisher applicationEventPublisher;

    FamilyService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void createPiggybank(UUID ownerId) {
        GrandchildId grandchildId = GrandchildId.of(ownerId);
        Piggybank piggybank = Piggybank.createForOwner(grandchildId);
        this.piggybanks.add(piggybank);
    }

    @Override
    public void deposit(UUID ownerId, Money amount) {
        this.piggybanks.stream()
                .filter(pb -> pb.isOwnedBy(GrandchildId.of(ownerId)))
                .findFirst()
                .ifPresent(pb -> {
                    pb.deposit(amount);
                    handleDomainEvents(pb);
                });
    }

    @EventListener
    private void handlePromiseLimitBoundaryReachedEvent(PromiseLimitBoundaryReachedEvent event) {
        log.error("PROMISE LIMIT REACHED, LET'S GO SHOPPING");
    }

    private void handleDomainEvents(Piggybank piggybank) {
        piggybank.getDomainEvents().forEach(applicationEventPublisher::publishEvent);
        piggybank.clearDomainEvents();
    }

}
