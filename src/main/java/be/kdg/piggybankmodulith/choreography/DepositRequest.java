package be.kdg.piggybankmodulith.choreography;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositRequest(UUID owner, BigDecimal amount) {


}
