package be.kdg.piggybankmodulith.family.port.in;

import java.util.Objects;
import java.util.UUID;

public record CreatePiggybankCommand(UUID ownerId) {

    public CreatePiggybankCommand {
        Objects.requireNonNull(ownerId, "Owner ID must not be null");
    }
}
