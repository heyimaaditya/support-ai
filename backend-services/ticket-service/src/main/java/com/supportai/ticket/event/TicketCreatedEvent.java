package com.supportai.ticket.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketCreatedEvent extends TicketBaseEvent {
    private String title;
    private String requesterEmail;
    private String priority;
}