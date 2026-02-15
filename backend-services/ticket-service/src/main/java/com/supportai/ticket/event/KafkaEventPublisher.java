package com.supportai.ticket.event;

import com.supportai.ticket.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishTicketCreated(TicketCreatedEvent event) {
        log.info("Publishing TicketCreatedEvent for ticket: {}", event.getTicketId());

        kafkaTemplate.send(KafkaConfig.TICKET_EVENTS_TOPIC, event.getTenantId(), event);
    }

    public void publishTicketUpdated(TicketUpdatedEvent event) {
        log.info("Publishing TicketUpdatedEvent for ticket: {}", event.getTicketId());
        kafkaTemplate.send(KafkaConfig.TICKET_EVENTS_TOPIC, event.getTenantId(), event);
    }
}