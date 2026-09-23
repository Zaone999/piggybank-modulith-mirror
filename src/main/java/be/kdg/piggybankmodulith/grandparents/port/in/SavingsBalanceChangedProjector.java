package be.kdg.piggybankmodulith.grandparents.port.in;

public interface SavingsBalanceChangedProjector {
    void project(SavingsDecreaseProjectionCommand projectionCommand);
    void project(SavingsIncreaseProjectionCommand projectionCommand);
}
