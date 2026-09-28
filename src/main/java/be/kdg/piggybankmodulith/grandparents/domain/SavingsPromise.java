package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SavingsPromise {

    private static final Logger logger = LoggerFactory.getLogger(SavingsPromise.class);

    public static final Money DEFAULT_PROMISE_BOUNDARY = Money.FIFTY;

    private final SavingsPromiseId savingsPromiseId;
    private final GrandChildId grandchildId;
    private Money balance;
    private final Money promiseBoundary;
    private boolean triggered;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static SavingsPromise promise(GrandChildId grandchildId,  Money promiseBoundary) {
        return new SavingsPromise(SavingsPromiseId.create(), grandchildId, Money.ZERO, promiseBoundary, false);
    }

    public static SavingsPromise restore(SavingsPromiseId savingsPromiseId, GrandChildId grandchildId, Money balance,
                                         Money promiseBoundary, boolean triggered) {
        return new SavingsPromise(savingsPromiseId, grandchildId, balance, promiseBoundary, triggered);
    }

    private SavingsPromise(SavingsPromiseId savingsPromiseId, GrandChildId grandchildId, Money balance,
                           Money promiseBoundary, boolean triggered) {
        if (balance.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Starting savings balance must not be negative");
        }

        if (promiseBoundary.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Promise boundary must not be negative");
        }

        this.savingsPromiseId = savingsPromiseId;
        this.grandchildId = grandchildId;
        this.balance = balance;
        this.promiseBoundary = promiseBoundary;
        this.triggered = triggered;
    }

    public void decreaseBalance(Money amount) {
        if (triggered) {
            return;
        }

        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Balance decrease must not be negative");
        }

        this.balance = balance.isLessThan(amount) ? Money.ZERO : balance.subtract(amount);

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
            this.domainEvents.add(new PromiseLimitBoundaryReachedEvent(grandchildId.uuid()));
        }

        logger.info("Promise Balance increased: {}", balance);
    }

    public boolean isMadeTo(GrandChildId grandchildId) {
        return this.grandchildId.equals(grandchildId);
    }

    public boolean notYetTriggered() {
        return !triggered;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public SavingsPromiseId getSavingsPromiseId() {
        return savingsPromiseId;
    }

    public GrandChildId getGrandchild() {
        return grandchildId;
    }

    public Money getBalance() {
        return balance;
    }

    public Money getPromiseBoundary() {
        return promiseBoundary;
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    @Override
    public String toString() {
        return "SavingsPromise{" +
                "savingsPromiseId=" + savingsPromiseId +
                ", grandchildId=" + grandchildId +
                ", balance=" + balance +
                '}';
    }
}
