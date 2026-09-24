package be.kdg.piggybankmodulith.family.domain;

import java.util.UUID;

public record GrandchildId(UUID uuid) {

    public static GrandchildId of(UUID uuid) {
        return new GrandchildId(uuid);
    }

}
