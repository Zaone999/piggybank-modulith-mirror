package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.Money;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public record SavingsEntry(UUID eventId, GrandChildId grandchildId, SavingsEntryType type, Money amount,
                           LocalDateTime pointInTime) {

    public SavingsEntry {
        Objects.requireNonNull(eventId, "Event ID must not be null");
        Objects.requireNonNull(grandchildId, "Grandchild ID must not be null");
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");
        Objects.requireNonNull(pointInTime, "Point in time must not be null");
    }

    public static SavingsEntry deposit(UUID eventId, GrandChildId grandchildId, Money amount, LocalDateTime pointInTime) {
        return new SavingsEntry(eventId, grandchildId, SavingsEntryType.DEPOSIT, amount, pointInTime);
    }

    public static SavingsEntry withdrawal(UUID eventId, GrandChildId grandchildId, Money amount, LocalDateTime pointInTime) {
        return new SavingsEntry(eventId, grandchildId, SavingsEntryType.WITHDRAWAL, amount, pointInTime);
    }
}
