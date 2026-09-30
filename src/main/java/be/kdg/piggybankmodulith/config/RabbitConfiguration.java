package be.kdg.piggybankmodulith.config;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.amqp.autoconfigure.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
class RabbitConfiguration {

    // only the events' package is trusted for the __TypeId__ header
    @Bean
    JacksonJsonMessageConverter jsonMessageConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper, DomainEvent.class.getPackageName());
    }

    // a send joins the use case's transaction: it goes out on commit, and not at all on rollback
    @Bean
    RabbitTemplateCustomizer transactedChannel() {
        return rabbitTemplate -> rabbitTemplate.setChannelTransacted(true);
    }
}
