package be.kdg.piggybankmodulith.family.adapter.in;

import java.util.UUID;

public record CreatePiggybankRequest(UUID ownerId) {

    public CreatePiggybankRequest {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID must not be null");
        }
    }
}
