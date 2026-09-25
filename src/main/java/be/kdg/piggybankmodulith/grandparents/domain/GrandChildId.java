package be.kdg.piggybankmodulith.grandparents.domain;

import java.util.UUID;

public record GrandChildId(UUID uuid) {

    public static GrandChildId of(UUID uuid) {
        return new GrandChildId(uuid);
    }

}
