package be.kdg.piggybankmodulith.grandparents.port.in;

import be.kdg.piggybankmodulith.common.Money;

import java.util.Objects;
import java.util.UUID;

public record CreateSavingsPromiseCommand(UUID grandchildId, Money promiseBoundary) {

    public CreateSavingsPromiseCommand {
        Objects.requireNonNull(grandchildId, "Grandchild ID must not be null");
        Objects.requireNonNull(promiseBoundary, "Promise boundary must not be null");
    }
}
