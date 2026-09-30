package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.grandparents.adapter.GrandparentsRabbitTopology;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

// sends after the commit; a failed send stays FAILED in the Event Publication Registry
@Component
class GrandparentsRabbitPublisherAdapter {

    private final RabbitTemplate rabbitTemplate;

    GrandparentsRabbitPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @ApplicationModuleListener
    void publish(PromiseLimitBoundaryReachedEvent event) {
        rabbitTemplate.convertAndSend(GrandparentsRabbitTopology.EXCHANGE,
                GrandparentsRabbitTopology.PROMISE_LIMIT_REACHED, event);
    }
}
