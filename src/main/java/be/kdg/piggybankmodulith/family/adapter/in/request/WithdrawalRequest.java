package be.kdg.piggybankmodulith.family.adapter.in.request;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawalRequest(UUID owner, BigDecimal amount) {
}
