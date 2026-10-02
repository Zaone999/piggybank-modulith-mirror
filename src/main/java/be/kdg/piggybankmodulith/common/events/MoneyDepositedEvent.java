package be.kdg.piggybankmodulith.common.events;

import be.kdg.piggybankmodulith.common.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public record MoneyDepositedEvent(LocalDateTime eventPit, UUID eventUUID, UUID piggybankId, UUID owner,
                                  Money amount) implements DomainEvent {

    public MoneyDepositedEvent(UUID piggybankId, UUID owner, Money amount) {
        this(LocalDateTime.now(), UUID.randomUUID(), piggybankId, owner, amount);
    }

    @Override
    public LocalDateTime eventPit() {
        return eventPit;
    }

    @Override
    public UUID eventUUID() {
        return eventUUID;
    }


}
