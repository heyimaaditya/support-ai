package com.supportai.ticket.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketAssignedEvent extends TicketBaseEvent {
    private Long assigneeId;
}