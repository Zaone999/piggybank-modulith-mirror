package be.kdg.piggybankmodulith.family.adapter.in.listeners;

import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotCommand;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class SpringApplicationEventListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SpringApplicationEventListenerAdapter.class);

    private final TakeSnapshotUseCase takeSnapshotUseCase;

    SpringApplicationEventListenerAdapter(TakeSnapshotUseCase takeSnapshotUseCase) {
        this.takeSnapshotUseCase = takeSnapshotUseCase;
    }

    // a special event is a good moment for a snapshot: only once the deposit that reached the promise is committed
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    void handlePromiseLimitBoundaryReachedEvent(PromiseLimitBoundaryReachedEvent event) {
        log.info("PROMISE LIMIT REACHED, LET'S GO SHOPPING");
        takeSnapshotUseCase.takeSnapshot(new TakeSnapshotCommand(event.grandchild(), event.eventPit()));
    }
}
