package be.kdg.piggybankmodulith.choreography;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.FamilyApi;
import be.kdg.piggybankmodulith.grandparents.GrandparentsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnBooleanProperty(name = "piggybank.drip-enabled")
public class ScheduledPiggybankDrip {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledPiggybankDrip.class);

    private final UUID BOBBY_ID = UUID.fromString("8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296");

    private final FamilyApi familyApi;
    private final GrandparentsApi grandparentsApi;

    public ScheduledPiggybankDrip(FamilyApi familyApi, GrandparentsApi grandparentsApi) {
        this.familyApi = familyApi;
        this.grandparentsApi = grandparentsApi;

        this.familyApi.createPiggybank(BOBBY_ID);
        this.grandparentsApi.createSavingsPromise(BOBBY_ID, Money.FIFTY);
    }

    @Scheduled(fixedRate = 10_000)
    void topUp() {
        this.familyApi.deposit(BOBBY_ID, Money.TEN);
    }
}
