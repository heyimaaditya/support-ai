package com.supportai.ticket.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketUpdatedEvent extends TicketBaseEvent {
    private String status;
}