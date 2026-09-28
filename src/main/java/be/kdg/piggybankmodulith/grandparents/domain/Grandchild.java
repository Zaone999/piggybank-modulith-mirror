package be.kdg.piggybankmodulith.grandparents.domain;

import java.util.Objects;

public record Grandchild(GrandChildId id, String firstName, String lastName) {

    public Grandchild {
        Objects.requireNonNull(id, "Grandchild ID must not be null");
        Objects.requireNonNull(firstName, "First name must not be null");
        Objects.requireNonNull(lastName, "Last name must not be null");
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
