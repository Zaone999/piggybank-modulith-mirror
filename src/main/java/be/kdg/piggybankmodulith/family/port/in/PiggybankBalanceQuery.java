package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.common.Money;

import java.util.UUID;

public interface PiggybankBalanceQuery {

    /**
     * Replays the entire event flow.
     */
    Money currentBalance(UUID ownerId);

    /**
     * Replays only the events of that month: deposits minus withdrawals, so it can be negative.
     */
    Money balanceForMonth(BalanceForMonth query);
}
