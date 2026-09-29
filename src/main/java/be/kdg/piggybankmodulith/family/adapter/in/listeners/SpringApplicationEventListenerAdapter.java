package be.kdg.piggybankmodulith.family.adapter.in.listeners;

import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class SpringApplicationEventListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SpringApplicationEventListenerAdapter.class);

    @ApplicationModuleListener
    void handlePromiseLimitBoundaryReachedEvent(PromiseLimitBoundaryReachedEvent event) {
        log.info("PROMISE LIMIT REACHED, LET'S GO SHOPPING");
    }
}
