package be.kdg.piggybankmodulith.choreography;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.FamilyApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnBooleanProperty(name = "piggybank.drip-enabled")
public class ScheduledPiggybankDrip {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledPiggybankDrip.class);

    private final UUID BOBBY_ID = UUID.fromString("8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296");

    private final FamilyApi familyApi;

    public ScheduledPiggybankDrip(FamilyApi familyApi) {
        this.familyApi = familyApi;
    }

    // once the application is ready, so the grandparents' listener receives the PiggybankCreatedEvent
    @EventListener(ApplicationReadyEvent.class)
    void createPiggybank() {
        this.familyApi.createPiggybank(BOBBY_ID);
    }

    @Scheduled(fixedRate = 2_500, initialDelay = 2_500)
    void topUp() {
        this.familyApi.deposit(BOBBY_ID, Money.TEN);
    }
}
