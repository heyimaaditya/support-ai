package com.supportai.ticket.dto;

import com.supportai.ticket.domain.TicketPriority;
import com.supportai.ticket.domain.TicketStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class TicketDTO {
    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private String category;
    private Long assigneeId;
    private Long requesterId;
    private String requesterEmail;
    private String tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
 
    private boolean aiProcessed;
    private String aiResponse;
    private BigDecimal aiConfidence;
}