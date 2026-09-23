package be.kdg.piggybankmodulith.grandparents.adapter.in;

import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class PromiseLimitBoundaryListener {

    private static final Logger logger = LoggerFactory.getLogger(PromiseLimitBoundaryListener.class);

    @ApplicationModuleListener
    public void promiseLimited(PromiseLimitBoundaryReachedEvent promiseLimitBoundaryReachedEvent) {
        // call the usecase that handles the shoppingtrip managements.

        logger.info("Limit of the promise reached {}", promiseLimitBoundaryReachedEvent.owner());
        System.exit(0);
    }
}
