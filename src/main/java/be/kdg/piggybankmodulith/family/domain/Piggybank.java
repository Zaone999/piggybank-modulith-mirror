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
import java.util.Objects;

/**
 * An event-sourced aggregate: it does not store its balance, it keeps every event that happened to it
 * and replays them to know where it is. Replayed in an {@link EntryWindow}, it only sees the events in
 * that window, and is read-only.
 */
public class Piggybank {

    private static final Logger LOGGER = LoggerFactory.getLogger(Piggybank.class);

    private final PiggybankId piggybankId;
    private final GrandchildId owner;

    // the history: the events that are stored, replayed when the piggybank is loaded
    private final List<DomainEvent> eventStore = new ArrayList<>();
    // what happened since: not stored or published yet
    private final List<DomainEvent> uncommittedEvents = new ArrayList<>();
    private final EntryWindow window;

    private Piggybank(PiggybankId piggybankId, GrandchildId owner, EntryWindow window) {
        this.piggybankId = Objects.requireNonNull(piggybankId, "Piggybank ID must not be null");
        this.owner = Objects.requireNonNull(owner, "Owner must not be null");
        this.window = Objects.requireNonNull(window, "Entry window must not be null");
    }

    public static Piggybank createForOwner(GrandchildId owner) {
        Piggybank piggybank = new Piggybank(PiggybankId.create(), owner, EntryWindow.all());
        piggybank.uncommittedEvents.add(new PiggybankCreatedEvent(owner.uuid()));
        return piggybank;
    }

    public static Piggybank replay(PiggybankId piggybankId, GrandchildId owner, List<DomainEvent> history) {
        return replay(piggybankId, owner, history, EntryWindow.all());
    }

    /**
     * Rebuilds a stored piggybank from the events in the window only: a read-only view on part of its history.
     */
    public static Piggybank replay(PiggybankId piggybankId, GrandchildId owner, List<DomainEvent> history,
                                   EntryWindow window) {
        Piggybank piggybank = new Piggybank(piggybankId, owner, window);
        history.stream()
                .filter(event -> window.contains(event.eventPit()))
                .forEach(piggybank.eventStore::add);
        return piggybank;
    }

    public boolean isOwnedBy(GrandchildId owner) {
        return this.owner.equals(owner);
    }

    public PiggybankId getPiggybankId() {
        return piggybankId;
    }

    public GrandchildId getOwner() {
        return owner;
    }

    public EntryWindow getWindow() {
        return window;
    }

    public Money getBalance() {
        Money balance = Money.ZERO;
        for (DomainEvent event : allEvents()) {
            balance = switch (event) {
                case PiggybankCreatedEvent created -> balance;
                case MoneyDepositedEvent deposited -> balance.add(deposited.amount());
                case MoneyWithdrawalEvent withdrawn -> balance.subtract(withdrawn.amount());
                default -> throw new IllegalStateException("Unknown piggybank event: " + event);
            };
        }
        return balance;
    }

    public void deposit(Money amount) {
        requireEntireHistory();
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Deposit amount must not be negative");
        }

        uncommittedEvents.add(new MoneyDepositedEvent(owner.uuid(), amount));
        LOGGER.info("Deposit to piggybank, current balance: {}", getBalance());
    }

    public void withdraw(Money amount) {
        requireEntireHistory();
        if (amount.isLessThan(Money.ZERO)) {
            throw new IllegalArgumentException("Withdrawal amount must not be negative");
        }

        Money balance = getBalance();
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + " from a balance of " + balance);
        }

        uncommittedEvents.add(new MoneyWithdrawalEvent(owner.uuid(), amount));
        LOGGER.info("Withdrawal from piggybank, current balance: {}", getBalance());
    }

    /**
     * The stored history that was replayed.
     */
    public List<DomainEvent> getEventStore() {
        return List.copyOf(eventStore);
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(uncommittedEvents);
    }

    public void clearDomainEvents() {
        eventStore.addAll(uncommittedEvents);
        uncommittedEvents.clear();
    }

    private List<DomainEvent> allEvents() {
        List<DomainEvent> all = new ArrayList<>(eventStore);
        all.addAll(uncommittedEvents);
        return all;
    }

    private void requireEntireHistory() {
        if (!window.isAll()) {
            // the rules need the balance of the entire history, not of part of it
            throw new IllegalStateException("A piggybank replayed for " + window + " is read-only");
        }
    }

    @Override
    public String toString() {
        return "Piggybank{" +
                "piggybankId=" + piggybankId +
                ", owner=" + owner +
                ", window=" + window +
                ", eventStore=" + eventStore +
                ", uncommittedEvents=" + uncommittedEvents +
                '}';
    }
}
