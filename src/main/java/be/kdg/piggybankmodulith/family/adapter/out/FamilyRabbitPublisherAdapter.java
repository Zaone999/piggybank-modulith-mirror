package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.FamilyMemberBornEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PiggybankCreatedEvent;
import be.kdg.piggybankmodulith.family.adapter.FamilyRabbitTopology;
import be.kdg.piggybankmodulith.family.domain.FamilyMember;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.out.FamilyMemberUpdatePort;
import be.kdg.piggybankmodulith.family.port.out.PiggybankUpdatePort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
class FamilyRabbitPublisherAdapter implements PiggybankUpdatePort, FamilyMemberUpdatePort {

    private final RabbitTemplate rabbitTemplate;

    FamilyRabbitPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void update(Piggybank piggybank) {
        publish(piggybank.getDomainEvents());
        piggybank.clearDomainEvents();
    }

    @Override
    public void update(FamilyMember familyMember) {
        publish(familyMember.getDomainEvents());
        familyMember.clearDomainEvents();
    }

    private void publish(List<DomainEvent> events) {
        events.forEach(event ->
                rabbitTemplate.convertAndSend(FamilyRabbitTopology.EXCHANGE, routingKeyOf(event), event));
    }

    private static String routingKeyOf(DomainEvent event) {
        return switch (event) {
            case FamilyMemberBornEvent ignored -> FamilyRabbitTopology.MEMBER_BORN;
            case PiggybankCreatedEvent ignored -> FamilyRabbitTopology.PIGGYBANK_CREATED;
            case MoneyDepositedEvent ignored -> FamilyRabbitTopology.MONEY_DEPOSITED;
            case MoneyWithdrawalEvent ignored -> FamilyRabbitTopology.MONEY_WITHDRAWN;
            default -> throw new IllegalArgumentException("Family does not publish " + event.getClass().getSimpleName());
        };
    }
}
