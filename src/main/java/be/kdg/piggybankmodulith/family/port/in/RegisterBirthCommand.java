package be.kdg.piggybankmodulith.family.port.in;

import java.util.Objects;
import java.util.UUID;

public record RegisterBirthCommand(UUID familyMemberId, String firstName, String lastName) {

    public RegisterBirthCommand {
        Objects.requireNonNull(familyMemberId, "Family member ID must not be null");
        Objects.requireNonNull(firstName, "First name must not be null");
        Objects.requireNonNull(lastName, "Last name must not be null");
    }
}
