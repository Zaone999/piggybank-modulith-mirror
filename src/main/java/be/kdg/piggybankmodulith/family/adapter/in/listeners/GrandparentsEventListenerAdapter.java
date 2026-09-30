package be.kdg.piggybankmodulith.family.adapter.in.listeners;

import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.family.adapter.FamilyRabbitTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class GrandparentsEventListenerAdapter {

    private static final Logger log = LoggerFactory.getLogger(GrandparentsEventListenerAdapter.class);

    @RabbitListener(queues = FamilyRabbitTopology.QUEUE)
    void handlePromiseLimitBoundaryReachedEvent(PromiseLimitBoundaryReachedEvent event) {
        log.info("PROMISE LIMIT REACHED, LET'S GO SHOPPING");
    }
}
