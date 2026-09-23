package be.kdg.piggybankmodulith.grandparents.domain;

import java.util.UUID;

public record GrandchildId(UUID uuid) {

    public static GrandchildId of(UUID uuid) {
        return new GrandchildId(uuid);
    }

}
