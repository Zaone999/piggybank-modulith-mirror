package be.kdg.piggybankmodulith.family.domain;

import java.util.UUID;

public record FamilyMemberId(UUID uuid) {

    public static FamilyMemberId of(UUID uuid) {
        return new FamilyMemberId(uuid);
    }
}
