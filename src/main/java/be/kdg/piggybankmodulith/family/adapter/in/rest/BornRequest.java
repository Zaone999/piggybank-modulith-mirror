package be.kdg.piggybankmodulith.family.adapter.in.rest;

import java.util.UUID;

public record BornRequest(UUID familyMemberId, String firstName, String lastName) {

    public BornRequest {
        if (familyMemberId == null) {
            throw new IllegalArgumentException("Family member ID must not be null");
        }
    }
}
