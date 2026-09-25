package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.family.domain.OwnerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SavingsPromise {

    private static final Logger logger = LoggerFactory.getLogger(SavingsPromise.class);

    private final OwnerId ownerId;
    private Money balance;
    private final Money promiseBoundary;
    private boolean triggered;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static SavingsPromise promise(OwnerId ownerId, Money promiseBoundary) {
        return new SavingsPromise(ownerId, Money.ZERO, promiseBoundary);
    }

    private SavingsPromise(OwnerId ownerId, Money balance, Money promiseBoundary) {
        if (balance.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Starting savings balance must not be negative");
        }

        if (promiseBoundary.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Promise boundary must not be negative");
        }

        this.ownerId = ownerId;
        this.balance = balance;
        this.promiseBoundary = promiseBoundary;
    }

    public void decreaseBalance(Money amount) {
        if (triggered) {
            return;
        }

        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Balance decrease must not be negative");
        }

        if (balance.isLessThan(amount)) {
            throw new IllegalStateException("Savings balance must not become negative");
        }

        this.balance = balance.subtract(amount);

        logger.info("Promise Balance decreased: {}", balance);
    }

    public void increaseBalance(Money amount) {
        if (triggered) {
            return;
        }

        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Balance increase must not be negative");
        }

        this.balance = balance.add(amount);

        if (balance.isAtLeast(promiseBoundary)) {
            triggered = true;
            this.domainEvents.add(new PromiseLimitBoundaryReachedEvent(ownerId.uuid()));
        }

        logger.info("Promise Balance increased: {}", balance);
    }

    public boolean isMadeTo(UUID grandchildId) {
        return this.ownerId.uuid().equals(grandchildId);
    }

    public boolean notYetTriggered() {
        return !triggered;
    }

    public OwnerId getGrandchild() {
        return ownerId;
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    @Override
    public String toString() {
        return "Savings{" +
                "grandchildId=" + ownerId +
                ", balance=" + balance +
                '}';
    }
}
