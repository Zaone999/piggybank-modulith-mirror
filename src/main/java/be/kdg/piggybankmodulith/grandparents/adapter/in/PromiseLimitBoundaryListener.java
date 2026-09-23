package be.kdg.piggybankmodulith.grandparents.adapter.in;

import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.grandparents.adapter.out.SavingsPromiseJpaAdapter;
import be.kdg.piggybankmodulith.grandparents.domain.OwnerId;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
public class PromiseLimitBoundaryListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ApplicationModuleListener
    public void promiseLimited(PromiseLimitBoundaryReachedEvent promiseLimitBoundaryReachedEvent) {
        // call the usecase that handles the shoppingtrip managements.

        logger.info("Limit of the promise reached {}", promiseLimitBoundaryReachedEvent.owner());
    }
}
