package be.kdg.piggybankmodulith.family.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Limits the view on the events of a piggybank: whole days, from and to both included.
 * {@link #all()} is as wide as necessary: the entire event flow.
 */
public record EntryWindow(LocalDate from, LocalDate to) {

    private static final EntryWindow ALL = new EntryWindow(null, null);

    public EntryWindow {
        if ((from == null) != (to == null)) {
            throw new IllegalArgumentException("An entry window needs both from and to, or neither");
        }
        if (from != null && to.isBefore(from)) {
            throw new IllegalArgumentException("An entry window cannot end before it starts");
        }
    }

    public static EntryWindow all() {
        return ALL;
    }

    public static EntryWindow between(LocalDate from, LocalDate to) {
        return new EntryWindow(from, to);
    }

    public static EntryWindow month(YearMonth month) {
        return new EntryWindow(month.atDay(1), month.atEndOfMonth());
    }

    public boolean isAll() {
        return from == null;
    }

    public LocalDateTime start() {
        return from.atStartOfDay();
    }

    public LocalDateTime end() {
        return to.plusDays(1).atStartOfDay();
    }

    public boolean contains(LocalDateTime pointInTime) {
        return isAll() || (!pointInTime.isBefore(start()) && pointInTime.isBefore(end()));
    }

    @Override
    public String toString() {
        return isAll() ? "all entries" : from + " to " + to;
    }
}
