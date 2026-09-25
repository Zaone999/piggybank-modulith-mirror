package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Piggybank {

    private final PiggybankId piggybankId;
    private final OwnerId owner;
    private Money balance;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(Piggybank.class);

    public Piggybank(OwnerId owner, Money balance) {
        this(PiggybankId.create(), owner, balance);
    }

    public Piggybank(PiggybankId piggybankId, OwnerId owner, Money balance) {
        if (balance.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Starting balance must not be negative");
        }

        this.piggybankId = piggybankId;
        this.owner = owner;
        this.balance = balance;
    }

    public static Piggybank createForOwner(OwnerId owner) {
        return new Piggybank(owner, Money.ZERO);
    }

    public boolean isOwnedBy(OwnerId owner) {
        return this.owner.equals(owner);
    }

    public void deposit(Money amount) {
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Deposit amount must not be negative");
        }

        this.balance = this.balance.add(amount);
        this.domainEvents.add(new MoneyDepositedEvent(owner.uuid(), amount));
        LOGGER.info("Deposit to piggybank, current balance: {}", balance);
    }

    public void withdraw(Money amount) {
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Withdrawal amount must not be negative");
        }

        if (balance.isLessThan(amount)) {
            throw new IllegalStateException("Cannot take money out");//this can be a better exception similar to the rest
        }

        this.balance = this.balance.subtract(amount);
        this.domainEvents.add(new MoneyWithdrawalEvent(owner.uuid(), amount));
        LOGGER.info("Withdrawal from piggybank, current balance: {}", balance);
    }


    @Override
    public String toString() {
        return "Piggybank{" +
                "piggybankId=" + piggybankId +
                ", owner=" + owner +
                ", balance=" + balance +
                ", domainEvents=" + domainEvents +
                '}';
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
