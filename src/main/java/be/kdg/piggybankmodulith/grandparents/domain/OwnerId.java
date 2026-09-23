package be.kdg.piggybankmodulith.grandparents.domain;

import java.util.UUID;

public record OwnerId(UUID uuid) {

    public static OwnerId of(UUID uuid) {
        return new OwnerId(uuid);
    }

}
