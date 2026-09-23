package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;

import java.util.ArrayList;
import java.util.List;

public class Piggybank {

    private final PiggybankId piggybankId;
    private final OwnerId owner;
    private Money balance;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Piggybank(OwnerId owner, Money balance) {
        this(PiggybankId.create(), owner, balance);
    }

    public Piggybank(PiggybankId piggybankId, OwnerId owner, Money balance) {
        this.piggybankId = piggybankId;
        this.owner = owner;
        this.balance = balance;
    }

    public static Piggybank createForOwner(OwnerId owner) {
        return new Piggybank(owner, Money.ZERO);
    }

    public PiggybankId getPiggybankId() {
        return piggybankId;
    }

    public OwnerId getOwner() {
        return owner;
    }

    public Money getBalance() {
        return balance;
    }

    public void deposit(Money amount) {
        this.balance = this.balance.add(amount);
        this.domainEvents.add(new MoneyDepositedEvent(owner.uuid(), amount));
    }

    public void withdraw(Money amount) {
        if (balance.subtract(amount).compareTo(Money.ZERO) < 0) {
            throw new IllegalStateException("Cannot take money out");//this can be a better exception similar to the rest
        }
        this.balance = this.balance.subtract(amount);
        this.domainEvents.add(new MoneyWithdrawalEvent(owner.uuid(), amount));
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
