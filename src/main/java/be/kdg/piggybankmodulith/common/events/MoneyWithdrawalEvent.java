package be.kdg.piggybankmodulith.common.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MoneyWithdrawalEvent(LocalDateTime eventPit, UUID eventUUID, UUID owner,  BigDecimal amount) implements DomainEvent {

    public MoneyWithdrawalEvent(UUID owner, BigDecimal amount){
        this(LocalDateTime.now(), UUID.randomUUID(), owner, amount);
    }

    @Override
    public LocalDateTime eventPit() {
        return eventPit;
    }

    public UUID eventUUID() {
        return eventUUID;
    }
}
