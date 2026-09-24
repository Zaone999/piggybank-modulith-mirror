package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SavingsPromise {
    private final GrandchildId grandchildId;
    private Money balance;
    private final Money promiseBoundary;

    private static final Logger logger = LoggerFactory.getLogger(SavingsPromise.class);

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static SavingsPromise promise(GrandchildId grandchildId,  Money promiseBoundary) {
        return new SavingsPromise(grandchildId, Money.ZERO, promiseBoundary);
    }

    private SavingsPromise(GrandchildId grandchildId, Money balance, Money promiseBoundary) {
        if (balance.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Starting savings balance must not be negative");
        }

        if (promiseBoundary.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Promise boundary must not be negative");
        }

        this.grandchildId = grandchildId;
        this.balance = balance;
        this.promiseBoundary = promiseBoundary;
    }

    public SavingsPromise decreaseBalance(Money amount) {
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Balance decrease must not be negative");
        }

        if (balance.isLessThan(amount)) {
            throw new IllegalStateException("Savings balance must not become negative");
        }

        this.balance = balance.subtract(amount);

        logger.info("Promise Balance decreased: {}", balance);
        return this;
    }

    public SavingsPromise increaseBalance(Money amount) {
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Balance increase must not be negative");
        }

        this.balance = balance.add(amount);

        if (balance.compareTo(promiseBoundary) >= 0) {
            balance = balance.subtract(promiseBoundary);
            this.domainEvents.add(new PromiseLimitBoundaryReachedEvent(grandchildId.uuid()));
        }

        logger.info("Promise Balance increased: {}", balance);
        return this;
    }


    public GrandchildId getGrandchild() {
        return grandchildId;
    }

    @Override
    public String toString() {
        return "Savings{" +
                "grandchildId=" + grandchildId +
                ", balance=" + balance +
                '}';
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
