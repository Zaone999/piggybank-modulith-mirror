package be.kdg.piggybankmodulith.family.adapter.out;

import be.kdg.piggybankmodulith.common.events.FamilyMemberBornEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PiggybankCreatedEvent;
import be.kdg.piggybankmodulith.family.adapter.FamilyRabbitTopology;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

// sends after the commit; a failed send stays FAILED in the Event Publication Registry
@Component
class FamilyRabbitPublisherAdapter {

    private final RabbitTemplate rabbitTemplate;

    FamilyRabbitPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @ApplicationModuleListener
    void publish(FamilyMemberBornEvent event) {
        rabbitTemplate.convertAndSend(FamilyRabbitTopology.EXCHANGE, FamilyRabbitTopology.MEMBER_BORN, event);
    }

    @ApplicationModuleListener
    void publish(PiggybankCreatedEvent event) {
        rabbitTemplate.convertAndSend(FamilyRabbitTopology.EXCHANGE, FamilyRabbitTopology.PIGGYBANK_CREATED, event);
    }

    @ApplicationModuleListener
    void publish(MoneyDepositedEvent event) {
        rabbitTemplate.convertAndSend(FamilyRabbitTopology.EXCHANGE, FamilyRabbitTopology.MONEY_DEPOSITED, event);
    }

    @ApplicationModuleListener
    void publish(MoneyWithdrawalEvent event) {
        rabbitTemplate.convertAndSend(FamilyRabbitTopology.EXCHANGE, FamilyRabbitTopology.MONEY_WITHDRAWN, event);
    }
}
