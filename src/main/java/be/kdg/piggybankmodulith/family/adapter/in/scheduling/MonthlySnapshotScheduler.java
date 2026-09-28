package be.kdg.piggybankmodulith.family.adapter.in.scheduling;

import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotUseCase;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotsCommand;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

/**
 * The passage of time as an input adapter: at the start of every month, a snapshot of every piggybank
 * covering everything before that month.
 */
@Component
class MonthlySnapshotScheduler {

    private final TakeSnapshotUseCase takeSnapshotUseCase;

    MonthlySnapshotScheduler(TakeSnapshotUseCase takeSnapshotUseCase) {
        this.takeSnapshotUseCase = takeSnapshotUseCase;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    void endOfMonth() {
        takeSnapshotUseCase.takeSnapshots(new TakeSnapshotsCommand(YearMonth.now().atDay(1).atStartOfDay()));
    }
}
