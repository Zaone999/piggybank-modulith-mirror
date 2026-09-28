package be.kdg.piggybankmodulith.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record FamilyMemberBornEvent(LocalDateTime eventPit, UUID eventUUID, UUID familyMemberId,
                                    String firstName, String lastName) implements DomainEvent {

    public FamilyMemberBornEvent(UUID familyMemberId, String firstName, String lastName) {
        this(LocalDateTime.now(), UUID.randomUUID(), familyMemberId, firstName, lastName);
    }
}
