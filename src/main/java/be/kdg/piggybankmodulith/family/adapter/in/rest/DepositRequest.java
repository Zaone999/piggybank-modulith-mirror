package be.kdg.piggybankmodulith.family.adapter.in.rest;

import java.math.BigDecimal;

public record DepositRequest(BigDecimal amount) {

    public DepositRequest {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
    }
}
