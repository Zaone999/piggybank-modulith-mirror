package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.family.domain.EntryWindow;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;

import java.util.List;
import java.util.Optional;

public interface PiggybankLoadPort {

    /**
     * Replays the entire event flow: the window is as wide as necessary.
     */
    default Optional<Piggybank> loadByOwnerId(GrandchildId ownerId) {
        return loadByOwnerId(ownerId, EntryWindow.all());
    }

    /**
     * Replays only the events in the window.
     */
    Optional<Piggybank> loadByOwnerId(GrandchildId ownerId, EntryWindow window);

    List<Piggybank> loadAll();
}
