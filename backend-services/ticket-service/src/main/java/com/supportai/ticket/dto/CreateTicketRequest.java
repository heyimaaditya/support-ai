package com.supportai.ticket.dto;

import com.supportai.ticket.domain.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTicketRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Priority is required")
    private TicketPriority priority;

    private String category;

    @NotNull(message = "Requester ID is required")
    private Long requesterId;
    
    @NotBlank(message = "Requester Email is required")
    private String requesterEmail;
}