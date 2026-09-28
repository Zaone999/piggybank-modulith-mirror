package be.kdg.piggybankmodulith.family.adapter.in.rest;

import be.kdg.piggybankmodulith.family.domain.readmodel.MonthlySavings;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record BestMonthResponse(UUID ownerId, YearMonth month, BigDecimal balance) {

    public static BestMonthResponse from(MonthlySavings monthlySavings) {
        return new BestMonthResponse(monthlySavings.owner().uuid(), monthlySavings.month(),
                monthlySavings.balance().value());
    }
}
