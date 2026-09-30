package be.kdg.piggybankmodulith.config;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
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
}
