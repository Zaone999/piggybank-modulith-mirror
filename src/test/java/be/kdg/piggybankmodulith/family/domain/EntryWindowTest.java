package be.kdg.piggybankmodulith.family.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntryWindowTest {

    private final EntryWindow firstQuarter = EntryWindow.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 31));

    @Test
    void containsBothDaysAndEverythingInBetween() {
        assertTrue(firstQuarter.contains(LocalDateTime.of(2026, 1, 1, 0, 0)));
        assertTrue(firstQuarter.contains(LocalDateTime.of(2026, 3, 31, 23, 59)));
        assertFalse(firstQuarter.contains(LocalDateTime.of(2025, 12, 31, 23, 59)));
        assertFalse(firstQuarter.contains(LocalDateTime.of(2026, 4, 1, 0, 0)));
    }

    @Test
    void aMonthRunsFromItsFirstToItsLastDay() {
        EntryWindow february = EntryWindow.month(YearMonth.of(2026, 2));

        assertTrue(february.contains(LocalDateTime.of(2026, 2, 1, 0, 0)));
        assertTrue(february.contains(LocalDateTime.of(2026, 2, 28, 23, 59)));
        assertFalse(february.contains(LocalDateTime.of(2026, 3, 1, 0, 0)));
    }

    @Test
    void allContainsEverything() {
        assertTrue(EntryWindow.all().isAll());
        assertTrue(EntryWindow.all().contains(LocalDateTime.of(1990, 1, 1, 0, 0)));
    }

    @Test
    void refusesAWindowThatEndsBeforeItStarts() {
        assertThrows(IllegalArgumentException.class,
                () -> EntryWindow.between(LocalDate.of(2026, 3, 31), LocalDate.of(2026, 1, 1)));
    }

    @Test
    void refusesAWindowWithOnlyOneEnd() {
        assertThrows(IllegalArgumentException.class, () -> new EntryWindow(LocalDate.of(2026, 1, 1), null));
    }
}
