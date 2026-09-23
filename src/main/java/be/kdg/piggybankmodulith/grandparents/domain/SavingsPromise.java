package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SavingsPromise {
    private OwnerId owner;
    private BigDecimal balance;

    public static final BigDecimal PROMISE_BOUNDARY = BigDecimal.valueOf(50L);

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public SavingsPromise(OwnerId owner, BigDecimal balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public SavingsPromise decreaseBalance(BigDecimal amount) {
        this.balance = balance.subtract(amount);
        return this;
    }

    public SavingsPromise increaseBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
        if (balance.compareTo(PROMISE_BOUNDARY) >= 0){
            balance = balance.subtract(PROMISE_BOUNDARY);
            this.domainEvents.add(new PromiseLimitBoundaryReachedEvent(owner.uuid()));
        }
        return this;
    }

    public OwnerId getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Savings{" +
                "owner=" + owner +
                ", balance=" + balance +
                '}';
    }

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }
}
