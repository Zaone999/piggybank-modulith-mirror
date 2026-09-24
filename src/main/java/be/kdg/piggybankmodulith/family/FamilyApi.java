package be.kdg.piggybankmodulith.family;

import be.kdg.piggybankmodulith.common.Money;

import java.util.UUID;

public interface FamilyApi {

    void createPiggybank(UUID ownerId);

    void deposit(UUID ownerId, Money amount);
}
