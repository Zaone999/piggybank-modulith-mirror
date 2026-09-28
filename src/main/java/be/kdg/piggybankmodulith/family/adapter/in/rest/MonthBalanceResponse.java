package be.kdg.piggybankmodulith.family.adapter.in.rest;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record MonthBalanceResponse(UUID ownerId, YearMonth month, BigDecimal balance) {
}
