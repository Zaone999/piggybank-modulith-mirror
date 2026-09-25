package be.kdg.piggybankmodulith.family.domain;

import java.util.UUID;

public record PiggybankId(UUID uuid) {

    public static PiggybankId of(UUID uuid) {
        return new PiggybankId(uuid);
    }

    public static PiggybankId create() {
        return new PiggybankId(UUID.randomUUID());
    }
}
