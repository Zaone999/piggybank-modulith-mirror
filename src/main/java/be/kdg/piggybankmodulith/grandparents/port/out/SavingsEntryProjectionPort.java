package be.kdg.piggybankmodulith.grandparents.port.out;

import be.kdg.piggybankmodulith.grandparents.domain.SavingsEntry;

public interface SavingsEntryProjectionPort {

    void save(SavingsEntry savingsEntry);
}
