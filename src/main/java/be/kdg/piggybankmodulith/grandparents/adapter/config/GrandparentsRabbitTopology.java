package be.kdg.piggybankmodulith.grandparents.adapter.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrandparentsRabbitTopology {

    public static final String EXCHANGE = "grandparents.events";
    public static final String PROMISE_LIMIT_REACHED = "promise.%s.limit-reached";

    public static final String QUEUE = "grandparents.family-events";
    private static final String DEAD_LETTER_EXCHANGE = "grandparents.dlx";
    private static final String DEAD_LETTER_QUEUE = QUEUE + ".dlq";

    private static final String FAMILY_EXCHANGE = "family.events";

    @Bean
    TopicExchange grandparentsEvents() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue grandparentsQueue() {
        return QueueBuilder.durable(QUEUE).deadLetterExchange(DEAD_LETTER_EXCHANGE).build();
    }

    @Bean
    FanoutExchange grandparentsDeadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Queue grandparentsDeadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    Binding grandparentsMemberBornBinding() {
        return familyEventBinding("member.*.born");
    }

    @Bean
    Binding grandparentsPiggyBankCreatedBinding() {
        return familyEventBinding("piggybank.*.created");
    }

    @Bean
    Binding grandparentsMoneyDepositedBinding() {
        return familyEventBinding("money.*.deposited");
    }

    @Bean
    Binding grandparentsMoneyWithdrawnBinding() {
        return familyEventBinding("money.*.withdrawn");
    }

    @Bean
    Binding grandparentsDeadLetterBinding() {
        return BindingBuilder.bind(grandparentsDeadLetterQueue()).to(grandparentsDeadLetterExchange());
    }

    private Binding familyEventBinding(String routingKey) {
        return BindingBuilder.bind(grandparentsQueue()).to(new TopicExchange(FAMILY_EXCHANGE)).with(routingKey);
    }
}
