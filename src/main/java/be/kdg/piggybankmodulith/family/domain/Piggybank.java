package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PiggybankCreatedEvent;
import be.kdg.piggybankmodulith.family.domain.exceptions.InsufficientFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Piggybank {

    private final PiggybankId piggybankId;
    private final GrandchildId owner;
    private Money balance;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(Piggybank.class);

    public Piggybank(GrandchildId owner, Money balance) {
        this(PiggybankId.create(), owner, balance);
    }

    public Piggybank(PiggybankId piggybankId, GrandchildId owner, Money balance) {
        if (balance.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Starting balance must not be negative");
        }

        this.piggybankId = piggybankId;
        this.owner = owner;
        this.balance = balance;
    }

    public static Piggybank createForOwner(GrandchildId owner) {
        Piggybank piggybank = new Piggybank(owner, Money.ZERO);
        piggybank.domainEvents.add(new PiggybankCreatedEvent(piggybank.piggybankId.uuid(), owner.uuid()));
        return piggybank;
    }

    public static Piggybank restore(PiggybankId piggybankId, GrandchildId owner, Money balance) {
        return new Piggybank(piggybankId, owner, balance);
    }

    public PiggybankId getPiggybankId() {
        return piggybankId;
    }

    public GrandchildId getOwner() {
        return owner;
    }

    public Money getBalance() {
        return balance;
    }

    public void deposit(Money amount) {
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Deposit amount must not be negative");
        }

        this.balance = this.balance.add(amount);
        this.domainEvents.add(new MoneyDepositedEvent(piggybankId.uuid(), owner.uuid(), amount));
        LOGGER.info("Deposit to piggybank, current balance: {}", balance);
    }

    public void withdraw(Money amount) {
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Withdrawal amount must not be negative");
        }

        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + " from a balance of " + balance);
        }

        this.balance = this.balance.subtract(amount);
        this.domainEvents.add(new MoneyWithdrawalEvent(piggybankId.uuid(), owner.uuid(), amount));
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
