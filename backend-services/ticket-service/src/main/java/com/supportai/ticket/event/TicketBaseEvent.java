package com.supportai.ticket.event;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public abstract class TicketBaseEvent {
    private Long ticketId;
    private String tenantId;
    private LocalDateTime timestamp = LocalDateTime.now();
}