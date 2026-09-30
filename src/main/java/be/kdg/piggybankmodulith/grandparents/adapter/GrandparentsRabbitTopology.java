package be.kdg.piggybankmodulith.grandparents.adapter;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrandparentsRabbitTopology {

    // what Grandparents publishes: its published language
    public static final String EXCHANGE = "grandparents.events";
    public static final String PROMISE_LIMIT_REACHED = "promise.limit-reached";

    // what Grandparents sees
    public static final String QUEUE = "grandparents.family-events";
    private static final String DEAD_LETTER_EXCHANGE = "grandparents.dlx";

    @Bean
    TopicExchange grandparentsEvents() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Declarables grandparentsQueue() {
        Queue queue = QueueBuilder.durable(QUEUE).deadLetterExchange(DEAD_LETTER_EXCHANGE).build();
        TopicExchange familyEvents = new TopicExchange("family.events");
        FanoutExchange deadLetterExchange = new FanoutExchange(DEAD_LETTER_EXCHANGE);
        Queue deadLetterQueue = QueueBuilder.durable(QUEUE + ".dlq").build();
        return new Declarables(queue, deadLetterExchange, deadLetterQueue,
                BindingBuilder.bind(queue).to(familyEvents).with("member.born"),
                BindingBuilder.bind(queue).to(familyEvents).with("piggybank.created"),
                BindingBuilder.bind(queue).to(familyEvents).with("money.deposited"),
                BindingBuilder.bind(queue).to(familyEvents).with("money.withdrawn"),
                BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange));
    }
}
