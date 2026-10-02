package be.kdg.piggybankmodulith.family.adapter.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FamilyRabbitTopology {

    public static final String EXCHANGE = "family.events";
    public static final String MEMBER_BORN = "member.%s.born";
    public static final String PIGGYBANK_CREATED = "piggybank.%s.created";
    public static final String MONEY_DEPOSITED = "money.%s.deposited";
    public static final String MONEY_WITHDRAWN = "money.%s.withdrawn";

    public static final String QUEUE = "family.grandparents-events";
    private static final String DEAD_LETTER_EXCHANGE = "family.dlx";
    private static final String DEAD_LETTER_QUEUE = QUEUE + ".dlq";

    private static final String GRANDPARENTS_EXCHANGE = "grandparents.events";
    private static final String PROMISE_LIMIT_REACHED = "promise.*.limit-reached";

    @Bean
    TopicExchange familyEvents() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue familyQueue() {
        return QueueBuilder.durable(QUEUE).deadLetterExchange(DEAD_LETTER_EXCHANGE).build();
    }

    @Bean
    FanoutExchange familyDeadLetterExchange() {
        return new FanoutExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Queue familyDeadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    Binding familyPromiseLimitReachedBinding() {
        return BindingBuilder.bind(familyQueue())
                .to(new TopicExchange(GRANDPARENTS_EXCHANGE))
                .with(PROMISE_LIMIT_REACHED);
    }

    @Bean
    Binding familyDeadLetterBinding() {
        return BindingBuilder.bind(familyDeadLetterQueue()).to(familyDeadLetterExchange());
    }
}
