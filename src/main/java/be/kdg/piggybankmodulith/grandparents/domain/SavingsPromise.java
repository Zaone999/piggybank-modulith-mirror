package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SavingsPromise {
    private final GrandchildId grandchild;
    private Money balance;
    private final Money promiseBoundary;

    private static final Money DEFAULT_PROMISE = Money.FIFTY;

    private static final Logger logger = LoggerFactory.getLogger(SavingsPromise.class);

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static SavingsPromise promise(GrandchildId grandchild) {
        return new SavingsPromise(grandchild, Money.ZERO, DEFAULT_PROMISE);
    }

    public SavingsPromise(GrandchildId grandchild, Money balance, Money promiseBoundary) {
        this.grandchild = grandchild;
        this.balance = balance;
        this.promiseBoundary = promiseBoundary;
    }

    public SavingsPromise decreaseBalance(Money amount) {
        this.balance = balance.subtract(amount);
        logger.info("Promise Balance decreased: {}", balance);
        return this;
    }

    public SavingsPromise increaseBalance(Money amount) {
        this.balance = balance.add(amount);
        if (balance.compareTo(promiseBoundary) >= 0){
            balance = balance.subtract(promiseBoundary);
            this.domainEvents.add(new PromiseLimitBoundaryReachedEvent(grandchild.uuid()));
        }
        logger.info("Promise Balance increased: {}", balance);
        return this;
    }

    public GrandchildId getGrandchild() {
        return grandchild;
    }

    @Override
    public String toString() {
        return "Savings{" +
                "grandchild=" + grandchild +
                ", balance=" + balance +
                '}';
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
}
