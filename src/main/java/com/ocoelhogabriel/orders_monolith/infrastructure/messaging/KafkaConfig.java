package com.ocoelhogabriel.orders_monolith.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    protected NewTopic pedidoCriadoTopic(@Value("${app.kafka.topic.pedidoCriado}") String topic) {
        return new NewTopic(topic, 1, (short) 1);
    }
}