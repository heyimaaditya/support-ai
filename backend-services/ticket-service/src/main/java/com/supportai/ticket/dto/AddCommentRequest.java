package com.supportai.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCommentRequest {
    @NotBlank(message = "Comment body cannot be empty")
    private String body;
    private boolean isInternal;
    private Long userId; 
    private String userName;
}