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

        // the database outlives a restart: Bobby's piggybank and promise may already be there
        try {
            this.familyApi.createPiggybank(BOBBY_ID);
        } catch (RuntimeException e) {
            logger.info("Piggybank not created: {}", e.getMessage());
        }

        try {
            this.grandparentsApi.createSavingsPromise(BOBBY_ID, Money.FIFTY);
        } catch (RuntimeException e) {
            logger.info("Savings promise not created: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRate = 2_500)
    void topUp() {
        this.familyApi.deposit(BOBBY_ID, Money.TEN);
    }
}
