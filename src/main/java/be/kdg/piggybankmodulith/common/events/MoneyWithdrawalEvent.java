package be.kdg.piggybankmodulith.common.events;

import be.kdg.piggybankmodulith.common.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public record MoneyWithdrawalEvent(LocalDateTime eventPit, UUID eventUUID, UUID piggybankId, UUID owner,  Money amount) implements DomainEvent {

    public MoneyWithdrawalEvent(UUID piggybankId, UUID owner, Money amount){
        this(LocalDateTime.now(), UUID.randomUUID(), piggybankId, owner, amount);
    }

}
