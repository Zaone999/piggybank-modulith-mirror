package be.kdg.piggybankmodulith.grandparents.adapter.out;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.grandparents.adapter.GrandparentsRabbitTopology;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import be.kdg.piggybankmodulith.grandparents.port.out.SavingsPromiseUpdatePort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
class GrandparentsRabbitPublisherAdapter implements SavingsPromiseUpdatePort {

    private final RabbitTemplate rabbitTemplate;

    GrandparentsRabbitPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void update(SavingsPromise savingsPromise) {
        savingsPromise.getDomainEvents().forEach(event ->
                rabbitTemplate.convertAndSend(GrandparentsRabbitTopology.EXCHANGE, routingKeyOf(event), event));
        savingsPromise.clearDomainEvents();
    }

    private static String routingKeyOf(DomainEvent event) {
        return switch (event) {
            case PromiseLimitBoundaryReachedEvent ignored -> GrandparentsRabbitTopology.PROMISE_LIMIT_REACHED;
            default -> throw new IllegalArgumentException("Grandparents does not publish " + event.getClass().getSimpleName());
        };
    }
}
