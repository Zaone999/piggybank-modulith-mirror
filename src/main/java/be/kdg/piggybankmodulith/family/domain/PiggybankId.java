package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;

import java.util.UUID;

public record PiggybankId(UUID uuid) {

    public static PiggybankId of(UUID uuid) {
        return new PiggybankId(uuid);
    }

    public static PiggybankId create() {
        return new PiggybankId(UUID.randomUUID());
    }

    public static PiggyBankNotFoundException notFoundForOwner(GrandchildId owner) {
        return new PiggyBankNotFoundException(String.format("Piggybank not found for owner %s", owner.uuid().toString()));
    }
}
