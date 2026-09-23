package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberNotFoundException;

import java.util.UUID;

public record GrandchildId(UUID uuid) {

    public static final GrandchildId BOBBY = GrandchildId.of(UUID.fromString("8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296"));

    public static GrandchildId of(UUID uuid) {
        return new GrandchildId(uuid);
    }

}
