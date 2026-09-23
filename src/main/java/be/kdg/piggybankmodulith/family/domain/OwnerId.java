package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberNotFoundException;

import java.util.UUID;

public record OwnerId(UUID uuid) {

    public static final OwnerId BOBBY = OwnerId.of(UUID.fromString("8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296"));

    public static OwnerId of(UUID uuid) {
        return new OwnerId(uuid);
    }

    public void notFound() throws FamilyMemberNotFoundException {
        throw new FamilyMemberNotFoundException(String.format("Family member with UUID %s was not found", this.uuid));
    }
}
