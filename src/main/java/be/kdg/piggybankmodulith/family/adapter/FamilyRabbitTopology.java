package be.kdg.piggybankmodulith.family.adapter;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FamilyRabbitTopology {

    // what Family publishes: its published language
    public static final String EXCHANGE = "family.events";
    public static final String MEMBER_BORN = "member.born";
    public static final String PIGGYBANK_CREATED = "piggybank.created";
    public static final String MONEY_DEPOSITED = "money.deposited";
    public static final String MONEY_WITHDRAWN = "money.withdrawn";

    // what Family sees
    public static final String QUEUE = "family.grandparents-events";
    private static final String DEAD_LETTER_EXCHANGE = "family.dlx";

    @Bean
    TopicExchange familyEvents() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Declarables familyQueue() {
        Queue queue = QueueBuilder.durable(QUEUE).deadLetterExchange(DEAD_LETTER_EXCHANGE).build();
        TopicExchange grandparentsEvents = new TopicExchange("grandparents.events");
        FanoutExchange deadLetterExchange = new FanoutExchange(DEAD_LETTER_EXCHANGE);
        Queue deadLetterQueue = QueueBuilder.durable(QUEUE + ".dlq").build();
        return new Declarables(queue, deadLetterExchange, deadLetterQueue,
                BindingBuilder.bind(queue).to(grandparentsEvents).with("promise.limit-reached"),
                BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange));
    }
}
