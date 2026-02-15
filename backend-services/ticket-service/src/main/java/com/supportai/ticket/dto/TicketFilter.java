package com.supportai.ticket.dto;

import com.supportai.ticket.domain.TicketPriority;
import com.supportai.ticket.domain.TicketStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketFilter {
    private List<TicketStatus> statuses;
    private List<TicketPriority> priorities;
    private String category;
    private Long assigneeId;
    private String searchTerm;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}