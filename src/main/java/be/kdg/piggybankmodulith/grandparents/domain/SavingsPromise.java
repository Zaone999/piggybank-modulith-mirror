package be.kdg.piggybankmodulith.grandparents.domain;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SavingsPromise {
    private final OwnerId owner;
    private BigDecimal balance;
    private final BigDecimal promiseBoundary;

    private static final Logger logger = LoggerFactory.getLogger(SavingsPromise.class);

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public static SavingsPromise promise(OwnerId owner,  BigDecimal promiseBoundary) {
        return new SavingsPromise(owner, BigDecimal.ZERO, promiseBoundary);
    }

    private SavingsPromise(OwnerId owner, BigDecimal balance, BigDecimal promiseBoundary) {
        this.owner = owner;
        this.balance = balance;
        this.promiseBoundary = promiseBoundary;
    }

    public SavingsPromise decreaseBalance(BigDecimal amount) {
        this.balance = balance.subtract(amount);
        logger.info("Promise Balance decreased: {}", balance);
        return this;
    }

    public SavingsPromise increaseBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
        if (balance.compareTo(promiseBoundary) >= 0){
            balance = balance.subtract(promiseBoundary);
            this.domainEvents.add(new PromiseLimitBoundaryReachedEvent(owner.uuid()));
        }
        logger.info("Promise Balance increased: {}", balance);
        return this;
    }

    public OwnerId getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Savings{" +
                "owner=" + owner +
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
