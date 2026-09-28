package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PiggybankCreatedEvent;
import be.kdg.piggybankmodulith.family.domain.exceptions.InsufficientFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An event-sourced aggregate: it does not store its balance, it keeps every event that happened to it
 * and replays them to know where it is. Replayed in an {@link EntryWindow}, it only sees the events in
 * that window, and is read-only.
 * <p>
 * Snapshotting: the balance of all events before {@code baseSnapshotDate} is kept in {@code baseSnapshotBalance},
 * so only the events from that date on are replayed.
 */
public class Piggybank {

    private static final Logger LOGGER = LoggerFactory.getLogger(Piggybank.class);

    private final PiggybankId piggybankId;
    private final GrandchildId owner;

    private final List<DomainEvent> eventStore = new ArrayList<>();
    private final List<DomainEvent> uncommittedEvents = new ArrayList<>();
    private final EntryWindow window;

    private Money baseSnapshotBalance = Money.ZERO;
    private LocalDateTime baseSnapshotDate;

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


    public static Piggybank replayFromSnapshot(PiggybankId piggybankId, GrandchildId owner,
                                               Money baseSnapshotBalance, LocalDateTime baseSnapshotDate,
                                               List<DomainEvent> eventsSinceSnapshot) {
        Piggybank piggybank = new Piggybank(piggybankId, owner, EntryWindow.all());
        piggybank.baseSnapshotBalance = Objects.requireNonNull(baseSnapshotBalance);
        piggybank.baseSnapshotDate = Objects.requireNonNull(baseSnapshotDate);
        eventsSinceSnapshot.stream()
                .filter(piggybank::isAfterSnapshot)
                .forEach(piggybank.eventStore::add);
        return piggybank;
    }

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
        return balanceOf(baseSnapshotBalance, allEvents());
    }

    public void takeSnapshot(LocalDateTime date) {
        requireEntireHistory();
        if (!uncommittedEvents.isEmpty()) {
            throw new IllegalStateException("Store the uncommitted events before taking a snapshot");
        }
        if (baseSnapshotDate != null && !date.isAfter(baseSnapshotDate)) {
            throw new IllegalArgumentException("A snapshot on " + date + " is not after the last one, on " + baseSnapshotDate);
        }

        List<DomainEvent> before = eventStore.stream().filter(event -> event.eventPit().isBefore(date)).toList();
        baseSnapshotBalance = balanceOf(baseSnapshotBalance, before);
        baseSnapshotDate = date;
        eventStore.removeAll(before);
        LOGGER.info("Snapshot of piggybank {} on {}: base balance {}", piggybankId.uuid(), date, baseSnapshotBalance);
    }

    public Money getBaseSnapshotBalance() {
        return baseSnapshotBalance;
    }

    public Optional<LocalDateTime> getBaseSnapshotDate() {
        return Optional.ofNullable(baseSnapshotDate);
    }

    private boolean isAfterSnapshot(DomainEvent event) {
        return baseSnapshotDate == null || !event.eventPit().isBefore(baseSnapshotDate);
    }

    private static Money balanceOf(Money snapshotBalance, List<DomainEvent> events) {
        Money balance = snapshotBalance;
        for (DomainEvent event : events) {
            balance = switch (event) {
                case PiggybankCreatedEvent created -> balance; //prevent it to go in default
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
            throw new IllegalStateException("A piggybank replayed for " + window + " is read-only");
        }
    }

    @Override
    public String toString() {
        return "Piggybank{" +
                "piggybankId=" + piggybankId +
                ", owner=" + owner +
                ", window=" + window +
                ", baseSnapshotBalance=" + baseSnapshotBalance +
                ", baseSnapshotDate=" + baseSnapshotDate +
                ", eventStore=" + eventStore +
                ", uncommittedEvents=" + uncommittedEvents +
                '}';
    }
}
