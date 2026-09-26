package be.kdg.piggybankmodulith.grandparents.domain;

import java.util.UUID;

public record SavingsPromiseId(UUID uuid) {

    public static SavingsPromiseId of(UUID uuid) {
        return new SavingsPromiseId(uuid);
    }

    public static SavingsPromiseId create() {
        return new SavingsPromiseId(UUID.randomUUID());
    }
}
