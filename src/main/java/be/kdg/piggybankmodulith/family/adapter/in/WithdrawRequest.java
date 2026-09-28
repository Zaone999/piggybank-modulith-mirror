package be.kdg.piggybankmodulith.family.adapter.in;

import java.math.BigDecimal;

public record WithdrawRequest(BigDecimal amount) {

    public WithdrawRequest {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
    }
}
