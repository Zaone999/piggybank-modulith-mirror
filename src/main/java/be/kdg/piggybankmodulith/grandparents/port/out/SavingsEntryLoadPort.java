package be.kdg.piggybankmodulith.grandparents.port.out;

import java.util.UUID;

public interface SavingsEntryLoadPort {

    boolean existsByEventId(UUID eventId);
}
