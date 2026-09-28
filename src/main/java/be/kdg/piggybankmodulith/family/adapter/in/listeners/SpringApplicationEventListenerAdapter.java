package be.kdg.piggybankmodulith.family.adapter.in.listeners;

import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.stereotype.Component;

@Component
class SpringApplicationEventListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SpringApplicationEventListenerAdapter.class);

    // only after the deposit is committed: exiting inside the transaction would lose it
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void handlePromiseLimitBoundaryReachedEvent(PromiseLimitBoundaryReachedEvent event) {
        log.info("PROMISE LIMIT REACHED, LET'S GO SHOPPING");
        System.exit(0);
    }
}
