package be.kdg.piggybankmodulith.family.adapter.in.response;

import java.math.BigDecimal;
import java.util.UUID;

public record PiggybankDto(UUID owner, BigDecimal amount) {
}
