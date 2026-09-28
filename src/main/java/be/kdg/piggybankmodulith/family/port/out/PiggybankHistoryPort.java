package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.common.events.DomainEvent;

import java.util.List;

public interface PiggybankHistoryPort {

    /**
     * Every stored event of every piggybank, oldest first: snapshots do not replace them.
     */
    List<DomainEvent> loadAllEvents();
}
