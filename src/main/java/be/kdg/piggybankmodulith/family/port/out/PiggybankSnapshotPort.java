package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.family.domain.Piggybank;

public interface PiggybankSnapshotPort {

    /**
     * Stores the piggybank's baseSnapshotBalance and baseSnapshotDate.
     */
    void saveSnapshot(Piggybank piggybank);
}
