package be.kdg.piggybankmodulith.family.port.in;

public interface TakeSnapshotUseCase {

    /**
     * A snapshot of one piggybank, for instance when its owner goes shopping.
     */
    void takeSnapshot(TakeSnapshotCommand command);

    /**
     * A snapshot of every piggybank, for instance at the end of the month.
     */
    void takeSnapshots(TakeSnapshotsCommand command);
}
